import type {Node} from "@xyflow/svelte";
import {writable, type Writable} from 'svelte/store';
import type {
    ActionButtonType,
    ActionLinkType,
    ActionsType,
    ActionType,
    ChangeScenariosActionType,
    EntryActionType,
    ForwardMessageActionType,
    ReadMessageActionType,
    SendContactActionType,
    SendFileActionType,
    SendMessageActionType
} from "./scenarios.types";

export function toActionFlow(action: ActionsType): ActionFlow<ActionsType> {
    return { flow: writable<ActionsType>(action) };
}

export type ActionFlow<T> = {
    flow: Writable<T>
}

// action helpers nodes
export type EntryActionNode = Node<ActionFlow<EntryActionType>>;
export type ActionLinkNode = Node<ActionFlow<ActionLinkType>>;
export type ActionButtonNode = Node<ActionFlow<ActionButtonType>>;

// actions nodes
export type ChangeScenariosNode = Node<ActionFlow<ChangeScenariosActionType>>;
export type ForwardMessageNode = Node<ActionFlow<ForwardMessageActionType>>;
export type ReadMessageNode = Node<ActionFlow<ReadMessageActionType>>;
export type SendContactNode = Node<ActionFlow<SendContactActionType>>;
export type SendFileNode = Node<ActionFlow<SendFileActionType>>;
export type SendMessageNode = Node<ActionFlow<SendMessageActionType>>;


//unions
export type ActionNodeType = Node<ActionFlow<ActionType>>
export type ActionsNodeType = Node<ActionFlow<ActionsType>>