import {EMPTY_STRIGN} from "./scenarios.constants";
import {ActionFlowNodeType} from "./scenarios.flow";
import type {
    ActionType,
    ChangeScenariosActionType,
    ForwardMessageActionType,
    ReadMessageActionType,
    SendContactActionType,
    SendFileActionType,
    SendMessageActionType
} from "./types/scenarios.types";

export function generateActionDataByType(type: ActionFlowNodeType): ActionType {
    switch (type) {
        case ActionFlowNodeType.CHANGE_SCENARIOS:
            return {
                name: "Изменить сценарий",
                id: crypto.randomUUID(),
                nextActionId: undefined,
                nextScenariosName: EMPTY_STRIGN,
                startFromBegin: false
            } satisfies ChangeScenariosActionType;
        case ActionFlowNodeType.FORWARD_MESSAGE:
            return {
                name: "Переслать сообщение",
                id: crypto.randomUUID(),
                nextActionId: undefined,
                forwardChatId: -1
            } satisfies ForwardMessageActionType;
        case ActionFlowNodeType.READ_MESSAGE:
            return {
                name: "Прочитать сообщение",
                id: crypto.randomUUID(),
                nextActionId: undefined,
                property: EMPTY_STRIGN,
                regex: undefined,
                waitingTime: EMPTY_STRIGN,
                timeoutMessage: EMPTY_STRIGN
            } satisfies ReadMessageActionType;
        case ActionFlowNodeType.SEND_CONTACT:
            return {
                name: "Отправить контакт",
                id: crypto.randomUUID(),
                nextActionId: undefined,
                phoneNumber: EMPTY_STRIGN,
                firstName: EMPTY_STRIGN,
                lastName: EMPTY_STRIGN
            } satisfies SendContactActionType;
        case ActionFlowNodeType.SEND_FILE:
            return {
                name: "Отправить файл",
                id: crypto.randomUUID(),
                nextActionId: undefined,
                fileId: EMPTY_STRIGN,
                fileName: EMPTY_STRIGN
            } satisfies SendFileActionType;
        case ActionFlowNodeType.SEND_MESSAGE:
            return {
                name: "Отправить сообщение",
                id: crypto.randomUUID(),
                nextActionId: undefined,
                buttons: [],
                text: EMPTY_STRIGN,
                isMarkdownText: false
            } satisfies SendMessageActionType;
        default:
            throw new Error('No such value')
    }
}