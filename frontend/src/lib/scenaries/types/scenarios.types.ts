import {type Static} from 'runtypes';
import type {
    ActionButtonRecord,
    ActionLinkRecord,
    ActionRecord,
    ChangeScenariosActionRecord,
    EntryActionRecord,
    ForwardMessageActionRecord,
    MatcherRecord,
    ReadMessageActionRecord,
    RouteRecord,
    ScenariosRecord,
    ScenariosStatus,
    SendContactActionRecord,
    SendFileActionRecord,
    SendMessageActionRecord
} from './scenarios.types.records';

// scenarios
export type MatcherType = Static<typeof MatcherRecord>
export type RouteType = Static<typeof RouteRecord>
export type ScenariosStatusType = Static<typeof ScenariosStatus>
export type ScenariosType = Static<typeof ScenariosRecord>

// actions helpers
export type ActionLinkType = Static<typeof ActionLinkRecord>
export type EntryActionType = Static<typeof EntryActionRecord>
export type ActionButtonType = Static<typeof ActionButtonRecord>

// actions
export type ChangeScenariosActionType = Static<typeof ChangeScenariosActionRecord>
export type ForwardMessageActionType = Static<typeof ForwardMessageActionRecord>
export type ReadMessageActionType = Static<typeof ReadMessageActionRecord>
export type SendContactActionType = Static<typeof SendContactActionRecord>
export type SendFileActionType = Static<typeof SendFileActionRecord>
export type SendMessageActionType = Static<typeof SendMessageActionRecord>
// all
export type ActionType = Static<typeof ActionRecord>

export type ActionsType = 
ActionLinkType |
EntryActionType |
ActionButtonType |
ChangeScenariosActionType |
ForwardMessageActionType |
ReadMessageActionType |
SendContactActionType |
SendFileActionType |
SendMessageActionType;
