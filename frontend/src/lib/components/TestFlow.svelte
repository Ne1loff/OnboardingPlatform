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
        type Node,
        type NodeTypes,
    } from "@xyflow/svelte";
    // 👇 this is important! You need to import the styles for Svelte Flow to work
    import { mainScenaries } from "$lib/scenaries/scenarios.constants";
    import {
        ActionFlowNodeType,
        createEdge,
        scenariosToFlow,
    } from "$lib/scenaries/scenarios.flow";
    import {
        toActionFlow,
        type ActionNodeType,
    } from "$lib/scenaries/types/scenarios.node.types";
    import type { ActionsType } from "$lib/scenaries/types/scenarios.types";
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
    } from "carbon-components-svelte";
    import { CopyFile, Paste } from "carbon-icons-svelte";
    import DeletableEdge from "./flow/edges/DeletableEdge.svelte";
    import ActionButtonNodeComponent from "./flow/nodes/ActionButtonNode.svelte";
    import ActionNodeComponent from "./flow/nodes/ActionNode.svelte";
    import EntryPointNodeComponent from "./flow/nodes/EntryPointNode.svelte";
    import LinkNodeComponent from "./flow/nodes/LinkNode.svelte";
    import Sidebar from "./sidebar/Sidebar.svelte";

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

    const { nodes, edges } = scenariosToFlow(mainScenaries);

    let contextIsOpen = false;
    let contextForNode: ActionNodeType | null = null;
    let nodeForCopy: ActionNodeType | null = null;

    //@ts-ignore
    function handleContextMenu({ detail: { event, node } }) {
        // Prevent native context menu from showing
        event.preventDefault();

        // Calculate position of the context menu. We want to make sure it
        // doesn't get positioned off-screen.
        console.log(node);

        contextForNode = node.parentId
            ? $nodes.find((it) => it.id === node.parentId)
            : node;
        contextIsOpen = true;
    }

    // Close the context menu if it's open whenever the window is clicked.
    function handlePaneClick() {
        contextForNode = null;
        contextIsOpen = false;
    }

    // TODO: copy of links
    // TODO: copy nodes with child (action buttons)
    const copyNode = () => {
        if (contextForNode === null) return;
        const selectedNode = contextForNode;

        nodeForCopy = {
            ...selectedNode,
            id: crypto.randomUUID(),
            position: {
                x: selectedNode.position.x,
                y: selectedNode.position.y + 100,
            },
        };
    };

    const pasteNode = () => {
        if (nodeForCopy === null) return;
        const selectedNode = nodeForCopy;

        nodes.update((nodes) => {
            nodes.push(selectedNode);
            return nodes;
        });

        nodeForCopy = null;
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
    };

    const deleteNode = () => {
        if (contextForNode === null) return;

        const selectedNode = contextForNode;
        nodes.update((nodes) =>
            nodes.filter((node) => node.id !== selectedNode.id),
        );
        edges.update((edges) =>
            edges.filter(
                (edge) =>
                    edge.source !== selectedNode.id &&
                    edge.target !== selectedNode.id,
            ),
        );
    };

    const onDragOver = (event: DragEvent) => {
        event.preventDefault();

        if (event.dataTransfer) {
            event.dataTransfer.dropEffect = "move";
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
        } satisfies ActionNodeType;

        nodes.update((old) => {
            old.push(node);

            return old;
        });
    };

    let sidebarIsOpen: boolean = false;
    let selectedNode: ActionNodeType | undefined = undefined;

    const onNodeClick = ({ detail }: CustomEvent<{ node: Node }>) => {
        const actionNode = detail.node as ActionNodeType;
        openSideBar(actionNode);
    };

    const openSideBar = (node: ActionNodeType) => {
        const { flow } = node.data;

        const unsubscribe = flow.subscribe((data) => {
            let actualNode: ActionNodeType = node;

            console.log("sidebar: ", actualNode, data);
            if (ActionRecord.guard(data)) {
                sidebarIsOpen = true;
                console.log("sidebar:ActionRecord");
            } else if (ActionLinkRecord.guard(data)) {
                sidebarIsOpen = true;
                actualNode =
                    $nodes.find((it) => it.id === data.link) ?? actualNode;

                console.log("sidebar:ActionLinkRecord");
            } else if (node.parentId) {
                sidebarIsOpen = true;
                actualNode =
                    $nodes.find((it) => it.id === node.parentId) ?? actualNode;
                console.log("sidebar:Children");
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
</script>

<SvelteFlow
    {nodes}
    {edges}
    {nodeTypes}
    {edgeTypes}
    fitView
    on:nodeclick={onNodeClick}
    on:nodecontextmenu={handleContextMenu}
    on:paneclick={handlePaneClick}
    on:dragover={onDragOver}
    on:drop={onDrop}
    onedgecreate={onEdgeCreate}
>
    <Panel position="top-left"><h1>Редактор маршрутов сценария</h1></Panel>
    <Panel position="top-right">
        {#if sidebarIsOpen && !!selectedNode}
            <Sidebar bind:open={sidebarIsOpen} node={selectedNode} />
        {/if}
    </Panel>
    <Controls />
    <Background variant={BackgroundVariant.Lines} />
    {#if contextForNode !== null}
        <ContextMenu
            bind:open={contextIsOpen}
            on:close={() => {
                contextForNode = null;
            }}
        >
            <ContextMenuOption
                indented
                labelText="Copy"
                shortcutText="CTRL + C"
                icon={CopyFile}
                disabled={contextForNode.id.includes(":link:")}
                on:click={copyNode}
            />
            <ContextMenuOption
                indented
                labelText="Paste"
                shortcutText="CTRL + V"
                icon={Paste}
                disabled={nodeForCopy === null}
                on:click={pasteNode}
            />
            <ContextMenuDivider />
            <ContextMenuOption indented labelText="Create">
                <ContextMenuGroup labelText="Create extra nodes">
                    <ContextMenuOption
                        labelText="Link to this node"
                        selectable={false}
                        on:click={createNodeLink}
                    />
                </ContextMenuGroup>
            </ContextMenuOption>
            <ContextMenuDivider />
            <ContextMenuOption
                indented
                kind="danger"
                labelText="Delete"
                on:click={deleteNode}
            />
        </ContextMenu>
    {/if}
</SvelteFlow>
