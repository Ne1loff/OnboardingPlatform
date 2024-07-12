import {type Connection, type Edge, MarkerType, Position} from "@xyflow/svelte";

import {writable, type Writable} from "svelte/store";
import type {ActionButtonNode, ActionNodeType} from "./types/scenarios.node.types";
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
    nodes: Writable<ActionNodeType[]>;
    edges: Writable<Edge[]>;
};

const getFlowType = ActionRecord.match(
    changeScenarios => ActionFlowNodeType.CHANGE_SCENARIOS,
    forwardMessage => ActionFlowNodeType.FORWARD_MESSAGE,
    readMessage => ActionFlowNodeType.READ_MESSAGE,
    sendContact => ActionFlowNodeType.SEND_CONTACT,
    sendFile => ActionFlowNodeType.SEND_FILE,
    sendMessage => ActionFlowNodeType.SEND_MESSAGE
);

const buildScenariosStartNode = (): ActionNodeType => ({
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
    const nodes: ActionNodeType[] = [buildScenariosStartNode(), ...scenatios.route.actions.flatMap((it): ActionNodeType[] => {
        actionMap.set(it.id, it);

        const result: ActionNodeType[] = [{
            id: it.id,
            type: getFlowType(it),
            data: { flow: writable(it) },
            position: { x: ++counter * 260, y: 0 }
        }];


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
    const nodesStore = writable<ActionNodeType[]>(nodes);

    const edges: Edge[] = scenatios.route.actions.filter(it => !!it.nextActionId).map(it => createEdge(createConnection(it.id, <string>it.nextActionId)));
    edges.push(...buttonsConnection.map(it => createEdge(it)));
    edges.push(createEdge(createConnection("start", scenatios.firstActionId)));

    const edgesStore = writable<Edge[]>(edges);

    return { nodes: nodesStore, edges: edgesStore }
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
