import {Array, Boolean, Literal, Nullish, Number, Record, String, Union} from 'runtypes';

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
    nextActionId: String.Or(Nullish),
    nextScenariosName: String,
    startFromBegin: Boolean,
});

export const ForwardMessageActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.Or(Nullish),
    forwardChatId: Number,
});

export const ReadMessageActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.Or(Nullish),
    property: String,
    regex: String.Or(Nullish),
    notificationMode: Union(Literal("ONCE"), Literal("EVERY")),
    waitingTime: String,
    timeoutMessage: String,
});

export const SendContactActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.Or(Nullish),
    phoneNumber: String,
    firstName: String,
    lastName: String,
});

export const SendFileActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.Or(Nullish),
    fileId: String,
    fileName: String,
});

export const SendMessageActionRecord = Record({
    id: String,
    name: String,
    nextActionId: String.Or(Nullish),
    buttons: Array(ActionButtonRecord),
    text: String,
    isMarkdownText: Boolean,
});

export const ActionRecord = Union(
    ChangeScenariosActionRecord,
    ForwardMessageActionRecord,
    ReadMessageActionRecord,
    SendContactActionRecord,
    SendFileActionRecord,
    SendMessageActionRecord
);

export const ActionLinkRecord = Record({
    link: String,
    name: String
});

export const MatcherRecord = Record({
    type: String,
    value: String
});

export const RouteRecord = Record({
    actions: Array(ActionRecord)
});

export const ScenariosRecord = Record({
    name: String,
    firstActionId: String,
    matchers: Array(MatcherRecord),
    route: RouteRecord
});
