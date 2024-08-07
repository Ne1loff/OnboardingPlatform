import {type Connection, type Edge, MarkerType, Position} from "@xyflow/svelte";

import {get, writable, type Writable} from "svelte/store";
import type {ActionButtonNode, ActionNodeType, ActionsNodeType} from "./types/scenarios.node.types";
import type {ActionButtonType, ActionType, EntryActionType, ScenariosType} from "./types/scenarios.types";
import {ActionRecord, SendMessageActionRecord} from "./types/scenarios.types.records";

export enum ActionFlowNodeType {
    ENTRY_POINT = "entry_point",
    ACTION_BUTTON = "button",
    ACTION_LINK = "link",
    CHANGE_SCENARIOS = "change_scenarios",
    FORWARD_MESSAGE = "forward_message",
    READ_MESSAGE = "read_message",
    SEND_CONTACT = "send_contact",
    SEND_FILE = "send_file",
    SEND_MESSAGE = "send_message"
}

export const CORE_ACTION_FLOW_NODE_TYPES = [
    ActionFlowNodeType.ACTION_LINK,
    ActionFlowNodeType.CHANGE_SCENARIOS,
    ActionFlowNodeType.FORWARD_MESSAGE,
    ActionFlowNodeType.READ_MESSAGE,
    ActionFlowNodeType.SEND_CONTACT,
    ActionFlowNodeType.SEND_FILE,
    ActionFlowNodeType.SEND_MESSAGE,
];

type ScenariosFlow = {
    nodes: Writable<ActionsNodeType[]>;
    edges: Writable<Edge[]>;
};

const getFlowType = ActionRecord.match(
    sendMessage => ActionFlowNodeType.SEND_MESSAGE,
    changeScenarios => ActionFlowNodeType.CHANGE_SCENARIOS,
    sendFile => ActionFlowNodeType.SEND_FILE,
    sendContact => ActionFlowNodeType.SEND_CONTACT,
    readMessage => ActionFlowNodeType.READ_MESSAGE,
    forwardMessage => ActionFlowNodeType.FORWARD_MESSAGE,
);

const buildScenariosStartNode = (): ActionsNodeType => ({
    id: "start",
    type: ActionFlowNodeType.ENTRY_POINT,
    data: { flow: writable<EntryActionType>({ name: "Входная точка" }) },
    position: { x: 0, y: 0 },
    sourcePosition: Position.Right,
})

export const createConnection = (source: string, target: string, targetHandle: string | null = null, sourceHandle: string | null = null): Connection => ({
    source,
    target,
    sourceHandle,
    targetHandle
})

export const getButtonNodeId = (parentId: string, buttonIndex: string | number): string => `${parentId}-b-${buttonIndex}`
export const getEdgeId = (source: string, target: string): string => `e-${source}-${target}`

export function scenariosToFlow(scenatios: ScenariosType): ScenariosFlow {
    const actionMap = new Map<String, ActionType>();

    let counter = 0;
    const buttonsConnection: Connection[] = []
    const nodes: ActionsNodeType[] = [buildScenariosStartNode(), ...scenatios.route.actions.flatMap((it): ActionsNodeType[] => {
        actionMap.set(it.id, it);
        
        const result: ActionsNodeType[] = [{
            id: it.id,
            type: getFlowType(it),
            data: { flow: writable(it) },
            position: { x: ++counter * 260, y: 0 }
        }];

        console.log(it, getFlowType(it));
        if (SendMessageActionRecord.guard(it)) {
            let index = 0;
            it.buttons.forEach(button => {
                const node: ActionButtonNode = createButtonNode(button, it.id, index++);
                result.push(node);
                buttonsConnection.push(createConnection(node.id, button.nextActionId));
            });
        }

        return result;
    })];
    const nodesStore = writable<ActionsNodeType[]>(nodes);

    const edges: Edge[] = scenatios.route.actions.filter(it => !!it.nextActionId).map(it => createEdge(createConnection(it.id, <string>it.nextActionId)));
    edges.push(...buttonsConnection.map(it => createEdge(it)));
    
    if (scenatios.firstActionId) {
        edges.push(createEntryEdge(createConnection("start", scenatios.firstActionId)));
    }

    const edgesStore = writable<Edge[]>(edges);

    return { nodes: nodesStore, edges: edgesStore }
}

export const flowToScenarios = (scenarios: ScenariosType, nodes: ActionsNodeType[]): ScenariosType => {
    const actions: ActionType[] = [];
    for (const node of nodes) {
        if (node.type !== ActionFlowNodeType.ACTION_LINK && CORE_ACTION_FLOW_NODE_TYPES.includes(<ActionFlowNodeType> node.type)) {
            const actionNode = <ActionNodeType> node;
            const data = get(actionNode.data.flow);
            actions.push({...data, nextActionId: getRealNextActionId(data.nextActionId)});
        }
    }

    scenarios.route.actions = actions;
    console.log(scenarios);
    
    return scenarios;
} 

export const getRealNextActionId = (nextActionId: string | null): string | null => {
    const linkIdentifier = ":link:";
    const linkIndex = nextActionId?.indexOf(linkIdentifier);
    if (linkIndex && linkIndex !== -1) {
        return nextActionId!.substring(0, linkIndex);
    }

    return nextActionId;
}

export const createButtonNode = (button: ActionButtonType, parentId: string, index: number): ActionButtonNode => {
    const id = getButtonNodeId(parentId, index);
    return {
        id,
        type: ActionFlowNodeType.ACTION_BUTTON,
        data: { flow: writable<ActionButtonType>({ name: button.name, nextActionId: button.nextActionId }) },
        sourcePosition: Position.Right,
        position: { x: 15, y: 50 + (2 * (index + 1)) + (40 * index) },
        parentId: parentId,
        extent: 'parent',
        draggable: false
    };
}

export const createEdge = ({ source, target, sourceHandle, targetHandle }: Connection): Edge => ({
    id: getEdgeId(source, target),
    source,
    target,
    sourceHandle,
    targetHandle,
    type: 'deletable',
    markerEnd: {
        type: MarkerType.ArrowClosed,
        width: 16,
        height: 16,
    }
});

export const createEntryEdge = ({ source, target, sourceHandle, targetHandle }: Connection): Edge => ({
    id: "entry-edge",
    source,
    target,
    sourceHandle,
    targetHandle,
    type: 'deletable',
    markerEnd: {
        type: MarkerType.ArrowClosed,
        width: 16,
        height: 16,
    }
});