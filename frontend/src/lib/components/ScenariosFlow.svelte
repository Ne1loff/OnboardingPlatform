<script lang="ts">
    import {
        addEdge,
        Background,
        BackgroundVariant,
        Controls,
        Panel,
        Position,
        SvelteFlow,
        useSvelteFlow,
        type Connection,
        type Edge,
        type Node,
        type NodeTypes,
        type XYPosition,
    } from "@xyflow/svelte";
    // 👇 this is important! You need to import the styles for Svelte Flow to work
    import { pushState } from "$app/navigation";
    import { resolveRoute } from "$app/paths";
    import {
        ActionFlowNodeType,
        createButtonNode,
        createConnection,
        createEdge,
        getButtonNodeId,
        scenariosToFlow,
    } from "$lib/scenaries/scenarios.flow";
    import {
        toActionFlow,
        type ActionNodeType,
        type ActionsNodeType,
    } from "$lib/scenaries/types/scenarios.node.types";
    import type {
        ActionsType,
        ScenariosStatusType,
        ScenariosType,
    } from "$lib/scenaries/types/scenarios.types";
    import {
        ActionButtonRecord,
        ActionLinkRecord,
        ActionRecord,
        SendMessageActionRecord,
    } from "$lib/scenaries/types/scenarios.types.records";
    import { generateActionDataByType } from "$lib/scenaries/utils";
    import "@xyflow/svelte/dist/style.css";
    import {
        ContextMenu,
        ContextMenuDivider,
        ContextMenuGroup,
        ContextMenuOption,
        InlineNotification,
    } from "carbon-components-svelte";
    import { CopyFile, Paste } from "carbon-icons-svelte";
    import TrashCan from "carbon-icons-svelte/lib/TrashCan.svelte";
    import { getContext, setContext, tick } from "svelte";
    import { derived, get, writable, type Writable } from "svelte/store";
    import DeletableEdge from "./flow/edges/DeletableEdge.svelte";
    import ActionButtonNodeComponent from "./flow/nodes/ActionButtonNode.svelte";
    import ActionNodeComponent from "./flow/nodes/ActionNode.svelte";
    import EntryPointNodeComponent from "./flow/nodes/EntryPointNode.svelte";
    import LinkNodeComponent from "./flow/nodes/LinkNode.svelte";
    import Sidebar from "./sidebar/Sidebar.svelte";

    const scenarios = getContext<Writable<ScenariosType>>("currentScenarios");
    const disabledEditing = derived<Writable<ScenariosType>, boolean>(
        scenarios,
        ($scenarios) =>
            $scenarios.status !== "DRAFT" && $scenarios.status !== "TEST",
    );

    setContext("scenariosDisabledEditing", disabledEditing);

    const nodeTypes: NodeTypes = {
        [ActionFlowNodeType.ENTRY_POINT]: EntryPointNodeComponent,
        [ActionFlowNodeType.ACTION_BUTTON]: ActionButtonNodeComponent,
        [ActionFlowNodeType.ACTION_LINK]: LinkNodeComponent,
        [ActionFlowNodeType.CHANGE_SCENARIOS]: ActionNodeComponent,
        [ActionFlowNodeType.FORWARD_MESSAGE]: ActionNodeComponent,
        [ActionFlowNodeType.READ_MESSAGE]: ActionNodeComponent,
        [ActionFlowNodeType.SEND_CONTACT]: ActionNodeComponent,
        [ActionFlowNodeType.SEND_FILE]: ActionNodeComponent,
        [ActionFlowNodeType.SEND_MESSAGE]: ActionNodeComponent,
    };

    const { screenToFlowPosition, getNode } = useSvelteFlow();
    const edgeTypes = {
        deletable: DeletableEdge,
    };
    let nodes: Writable<ActionsNodeType[]> = writable([]);
    let edges: Writable<Edge[]> = writable([]);

    const updateFlow = async (scenarios: ScenariosType) => {
        const flow = scenariosToFlow(scenarios);
        edges.set([]);
        nodes.set([]);

        await tick();
        const unsubscribeEdges = flow.edges.subscribe((it) => edges.set(it));
        const unsubscribeNodes = flow.nodes.subscribe((it) => nodes.set(it));

        unsubscribeEdges();
        unsubscribeNodes();
    };

    $: updateFlow($scenarios);

    let contextIsOpen = false;
    let contextForNode: ActionNodeType | null = null;
    let actionForNode: ActionNodeType | null = null;

    function handleContextMenu({
        detail: { event, node },
    }: CustomEvent<{ event: MouseEvent | TouchEvent; node: Node }>) {
        event.preventDefault();

        if (node.type === ActionFlowNodeType.ENTRY_POINT) {
            return;
        }

        contextForNode = <ActionNodeType>(
            (node.parentId
                ? $nodes.find((it) => it.id === node.parentId)
                : node)
        );
        contextIsOpen = true;
    }

    let pastePossition: XYPosition | undefined;

    function handlePaneContextMenu({
        detail: { event },
    }: CustomEvent<{ event: MouseEvent }>) {
        event.preventDefault();

        contextForNode = null;
        pastePossition = screenToFlowPosition({
            x: event.clientX,
            y: event.clientY,
        });
        contextIsOpen = true;
    }

    // Close the context menu if it's open whenever the window is clicked.
    function handlePaneClick() {
        contextForNode = null;
        contextIsOpen = false;
    }

    const copyNode = () => {
        if (contextForNode === null) return;
        const selectedNode = contextForNode;

        const selectedNodeData = get(selectedNode.data.flow);
        const id = crypto.randomUUID();

        const flowData = { ...structuredClone(selectedNodeData), id: id };

        actionForNode = {
            ...selectedNode,
            id: id,
            position: {
                x: selectedNode.position.x,
                y: selectedNode.position.y + 100,
            },
            data: {
                flow: writable(flowData),
            },
        };
        contextForNode = null;
    };

    const pasteNode = () => {
        if (actionForNode === null) return;
        const selectedNode = actionForNode;
        const selectedNodeData = get(selectedNode.data.flow);

        if (pastePossition) {
            selectedNode.position = { ...pastePossition };
        }

        const buttonEdges: Edge[] = [];
        nodes.update((nodes) => {
            nodes.push(selectedNode);

            if (!SendMessageActionRecord.guard(selectedNodeData)) {
                return nodes;
            }

            const buttons = selectedNodeData.buttons;
            if (buttons.length === 0) {
                return nodes;
            }

            for (let i = 0; i < buttons.length; i++) {
                const button = buttons[i];

                const buttonNode = createButtonNode(
                    button,
                    selectedNodeData.id,
                    i,
                );

                buttonEdges.push(
                    createEdge(
                        createConnection(buttonNode.id, button.nextActionId),
                    ),
                );
                nodes.push(buttonNode);
            }

            return nodes;
        });

        edges.update((edges) => {
            if (buttonEdges.length > 0) {
                edges.push(...buttonEdges);
            }

            if (selectedNodeData.nextActionId) {
                const newEdge = createEdge(
                    createConnection(
                        selectedNode.id,
                        selectedNodeData.nextActionId!,
                    ),
                );
                edges.push(newEdge);
            }

            return edges;
        });

        contextForNode = null;
        actionForNode = null;
    };

    const createNodeLink = () => {
        if (contextForNode === null) return;
        const selectedNode = contextForNode;

        nodes.update((nodes) => {
            let actionData: ActionsType | undefined;
            const { flow } = selectedNode.data;

            const unsubscribe = flow.subscribe((it) => (actionData = it));

            if (!actionData) {
                unsubscribe();
                return nodes;
            }

            if (ActionLinkRecord.guard(actionData)) {
                nodes.push({
                    id: `${actionData.link}:link:${crypto.randomUUID()}`,
                    position: {
                        x: selectedNode.position.x,
                        y: selectedNode.position.y + 100,
                    },
                    type: ActionFlowNodeType.ACTION_LINK,
                    data: toActionFlow({
                        link: actionData.link,
                        name: actionData.name,
                    }),
                    sourcePosition: Position.Left,
                });
            } else if (ActionRecord.guard(actionData)) {
                nodes.push({
                    id: `${selectedNode.id}:link:${crypto.randomUUID()}`,
                    position: {
                        x: selectedNode.position.x,
                        y: selectedNode.position.y + 100,
                    },
                    type: ActionFlowNodeType.ACTION_LINK,
                    data: toActionFlow({
                        link: actionData.id,
                        name: actionData.name,
                    }),
                    sourcePosition: Position.Left,
                });
            }

            unsubscribe();
            return nodes;
        });

        contextForNode = null;
        actionForNode = null;
    };

    const deleteNode = () => {
        if (contextForNode === null) return;

        const selectedNode = contextForNode;
        const selectedNodeData = get(contextForNode.data.flow);

        const nodesIdToDelete = new Set<string>();

        nodesIdToDelete.add(selectedNode.id);

        if (SendMessageActionRecord.guard(selectedNodeData)) {
            const buttons = selectedNodeData.buttons;
            for (let index = 0; index < buttons.length; index++) {
                const buttonNodeId = getButtonNodeId(selectedNode.id, index);
                nodesIdToDelete.add(buttonNodeId);
            }
        }

        edges.update((edges) =>
            edges.filter(
                (edge) =>
                    !nodesIdToDelete.has(edge.source) &&
                    !nodesIdToDelete.has(edge.target),
            ),
        );

        nodes.update((nodes) =>
            nodes.filter((node) => !nodesIdToDelete.has(node.id)),
        );

        contextForNode = null;
        actionForNode = null;
    };

    const onDragOver = (event: DragEvent) => {
        event.preventDefault();

        if (event.dataTransfer) {
            event.dataTransfer.dropEffect = $disabledEditing ? "none" : "move";
        }
    };

    const onDrop = (event: DragEvent) => {
        event.preventDefault();

        if (!event.dataTransfer) {
            return null;
        }

        const type = event.dataTransfer.getData(
            "application/svelteflow",
        ) as ActionFlowNodeType;

        const position = screenToFlowPosition({
            x: event.clientX,
            y: event.clientY,
        });

        const data = generateActionDataByType(type);
        const node = {
            id: data.id,
            type: type,
            data: toActionFlow(data),
            position: position,
            sourcePosition: Position.Right,
        } satisfies ActionsNodeType;

        nodes.update((old) => {
            old.push(node);

            return old;
        });
    };

    let sidebarIsOpen: boolean = false;
    let selectedNode: ActionNodeType | undefined = undefined;

    const onNodeClick = ({ detail }: CustomEvent<{ node: Node }>) => {
        if (detail.node.type === ActionFlowNodeType.ENTRY_POINT) {
            pushState(
                resolveRoute("/scenaries/[scenarios]/settings", {
                    scenarios: $scenarios.id,
                }),
                { showSettings: true },
            );
            return;
        }

        const actionNode = detail.node as ActionNodeType;
        openSideBar(actionNode);
    };

    const openSideBar = (node: ActionNodeType) => {
        const { flow } = node.data;

        const unsubscribe = flow.subscribe((data) => {
            let actualNode: ActionNodeType = node;

            if (node.parentId) {
                sidebarIsOpen = true;
                actualNode =
                    <ActionNodeType>(
                        $nodes.find((it) => it.id === node.parentId)
                    ) ?? actualNode;
            } else if (ActionLinkRecord.guard(data)) {
                sidebarIsOpen = true;
                actualNode =
                    <ActionNodeType>$nodes.find((it) => it.id === data.link) ??
                    actualNode;
            } else if (ActionRecord.guard(data)) {
                sidebarIsOpen = true;
            }

            selectedNode = actualNode;
        });

        unsubscribe();
    };

    const onEdgeCreate = (connection: Connection) => {
        const newEdge = createEdge(connection);
        edges.update((edges) => addEdge(newEdge, edges));

        const actionNode = <ActionNodeType>getNode(connection.source);
        if (actionNode.parentId) {
            let parentNode = <ActionNodeType>getNode(actionNode.parentId);
            const ids = connection.source.split("-b-");
            const btnIndex = ids[ids.length - 1];
            parentNode.data.flow.update((it) => {
                if (SendMessageActionRecord.guard(it)) {
                    it.buttons[Number(btnIndex)].nextActionId =
                        connection.target;
                }
                return it;
            });
            actionNode.data.flow.update((it) => {
                if (ActionButtonRecord.guard(it)) {
                    it.nextActionId = connection.target;
                }
                return it;
            });
        } else {
            actionNode.data.flow.update((it) => {
                if (ActionRecord.guard(it)) {
                    it.nextActionId = connection.target;
                }
                return it;
            });
        }
    };

    const statuses: { key: ScenariosStatusType; text: string }[] = [
        { key: "PUBLISHED", text: "Опубликован" },
        { key: "TEST", text: "Тестовый режим" },
        { key: "DRAFT", text: "Черновик" },
        { key: "ARCHIVED", text: "Архивирован" },
    ];
</script>

<div style:height="100%">
    {#if $disabledEditing}
        <InlineNotification
            hideCloseButton
            kind="warning"
            title="Ограничено редактирование:"
            subtitle={`Стутус сценария "${statuses.find((it) => it.key === $scenarios.status)?.text}". Даный статус запрещает редактирование!`}
            style="position: absolute;margin-left: 16px;z-index: 1;"
        />
    {/if}
    <SvelteFlow
        {nodes}
        {edges}
        {nodeTypes}
        {edgeTypes}
        fitView
        on:nodeclick={onNodeClick}
        on:nodecontextmenu={handleContextMenu}
        on:paneclick={handlePaneClick}
        on:panecontextmenu={handlePaneContextMenu}
        on:dragover={onDragOver}
        on:drop={onDrop}
        onedgecreate={onEdgeCreate}
        nodesDraggable={!$disabledEditing}
        nodesConnectable={!$disabledEditing}
        elementsSelectable={!$disabledEditing}
    >
        <Panel position="top-left"><h1 style="user-select: none;">Редактор маршрутов сценария</h1></Panel>
        <Panel position="top-right">
            {#if sidebarIsOpen && !!selectedNode}
                <Sidebar bind:open={sidebarIsOpen} node={selectedNode} />
            {/if}
        </Panel>
        <Controls showLock={!$disabledEditing} />
        <Background variant={BackgroundVariant.Lines} />

        {#if contextIsOpen}
            <ContextMenu
                bind:open={contextIsOpen}
                on:close={() => {
                    contextForNode = null;
                }}
            >
                <ContextMenuOption
                    indented
                    labelText="Копировать"
                    icon={CopyFile}
                    disabled={contextForNode?.id.includes(":link:") ?? true}
                    on:click={copyNode}
                />
                <ContextMenuOption
                    indented
                    labelText="Вставить"
                    icon={Paste}
                    disabled={actionForNode === null}
                    on:click={pasteNode}
                />
                <ContextMenuDivider />
                <ContextMenuOption
                    indented
                    labelText="Создать"
                    disabled={contextForNode === null}
                >
                    <ContextMenuGroup>
                        <ContextMenuOption
                            labelText="Ссылку на это действие"
                            on:click={createNodeLink}
                        />
                    </ContextMenuGroup>
                </ContextMenuOption>
                <ContextMenuDivider />
                <ContextMenuOption
                    indented
                    kind="danger"
                    labelText="Удалить"
                    disabled={contextForNode === null}
                    icon={TrashCan}
                    on:click={deleteNode}
                />
            </ContextMenu>
        {/if}
    </SvelteFlow>
</div>
