import {Array, Boolean, Literal, Number, Record, String, Union} from 'runtypes';

export const EntryActionRecord = Record({
    name: String
});

export const ActionButtonRecord = Record({
    name: String,
    nextActionId: String
});

export const ChangeScenariosActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.nullable(),
    nextScenarioRouteDefinitionId: String.nullable(),
    startFromBegin: Boolean,
});

export const ForwardMessageActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.nullable(),
    forwardChatId: Number,
});

export const ReadMessageActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.nullable(),
    property: String,
    regex: String.nullable(),
    notificationMode: Union(Literal("ONCE"), Literal("EVERY"), Literal("DISABLED")),
    waitingTime: String.nullable(),
    timeoutMessage: String,
});

export const SendContactActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.nullable(),
    phoneNumber: String,
    firstName: String,
    lastName: String,
});

export const SendFileActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.nullable(),
    fileId: String.nullable(),
    fileName: String.nullable(),
});

export const SendMessageActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.nullable(),
    buttons: Array(ActionButtonRecord),
    text: String,
    markdownText: Boolean,
});

export const ActionRecord = Union(
    SendMessageActionRecord,
    ChangeScenariosActionRecord,
    SendFileActionRecord,
    SendContactActionRecord,
    ReadMessageActionRecord,
    ForwardMessageActionRecord,
);

export const ActionLinkRecord = Record({
    link: String,
    name: String
});

export const MatcherRecord = Record({
    type: Union(Literal("COMMAND"), Literal("TEXT")),
    value: String
});

export const RouteRecord = Record({
    actions: Array(ActionRecord)
});

export const ScenariosStatus = Union(Literal("DRAFT"), Literal("TEST"), Literal("PUBLISHED"), Literal("ARCHIVED"));

export const ScenariosRecord = Record({
    id: String,
    name: String,
    firstActionId: String.nullable(),
    matcher: MatcherRecord,
    route: RouteRecord,
    status: ScenariosStatus,
});
