import type {ScenariosType} from "./types/scenarios.types";

export const EMPTY_STRIGN = "$$__$NULL$__$$";

export const mainScenaries: ScenariosType = {
    "name": "main-scenarios",
    "firstActionId": "c38655e0-cf1a-444d-b0be-e15c7a2179b8",
    "matchers": [
        {
            "type": "COMMAND",
            "value": "/start"
        }
    ],
    "route": {
        "actions": [
            {
                "id": "c38655e0-cf1a-444d-b0be-e15c7a2179b8",
                "nextActionId": "1f268027-6601-45a6-b9cc-629407557af8",
                "name": "Приветствие",
                "text": "Привет, я помощник компании \"НПЦ КСБ\" и \"КСБ-СОФТ\". Я помогаю новым сотрудникам освоиться.\nПожалуйста, подскажи, как тебя зовут?",
                "isMarkdownText": false,
                "buttons": []
            },
            {
                "id": "1f268027-6601-45a6-b9cc-629407557af8",
                "nextActionId": "c860953b-9043-4464-b98b-82f0e6723059",
                "name": "Получение вопроса",
                "property": "user_name",
                "regex": undefined,
                "waitingTime": "PT5M",
                "timeoutMessage": "Пожалуйста, скажи как тебя зовут)"
            },
            {
                "id": "c860953b-9043-4464-b98b-82f0e6723059",
                "nextActionId": undefined,
                "name": "Главное меню",
                "text": "Привет, $user_name, приятно познакомится!\nМожет я могу чем-то помочь?",
                "isMarkdownText": false,
                "buttons": [
                    {
                        "name": "Задать вопрос",
                        "nextActionId": "339a9c70-af30-48b6-8b89-ccb9f06075e4"
                    },
                    {
                        "name": "Контакты HR",
                        "nextActionId": "629c7a2a-d36a-4c55-8157-0bb99e8958d2"
                    },
                    {
                        "name": "Документы",
                        "nextActionId": "72b196c1-032f-46ba-a81e-2717b3722d55"
                    }, {
                        "name": "Как пройти",
                        "nextActionId": "0362ab74-20c1-44e5-82ee-ef77c9999b29"
                    },
                ]
            },
            {
                "id": "339a9c70-af30-48b6-8b89-ccb9f06075e4",
                "nextActionId": "c860953b-9043-4464-b98b-82f0e6723059",
                "name": "Запустить сценарий - Задать вопрос",
                "nextScenariosName": "question-scenarios",
                "startFromBegin": true
            },
            {
                "id": "629c7a2a-d36a-4c55-8157-0bb99e8958d2",
                "nextActionId": "c860953b-9043-4464-b98b-82f0e6723059",
                "name": "Запустить сценарий - Контакты HR",
                "nextScenariosName": "",
                "startFromBegin": true
            },
            {
                "id": "72b196c1-032f-46ba-a81e-2717b3722d55",
                "nextActionId": "c860953b-9043-4464-b98b-82f0e6723059",
                "name": "Запустить сценарий - Документы",
                "nextScenariosName": "documents-scenarios",
                "startFromBegin": true
            },
            {
                "id": "0362ab74-20c1-44e5-82ee-ef77c9999b29",
                "nextActionId": "c860953b-9043-4464-b98b-82f0e6723059",
                "name": "Запустить сценарий - Как пройти",
                "nextScenariosName": "",
                "startFromBegin": true
            },
        ]
    }
}

export const documentsScenarios: ScenariosType = {
    "name": "documents-scenarios",
    "firstActionId": "e2e1b674-dfbc-4c55-abf8-2055d07bbf5b",
    "matchers": [
        {
            "type": "COMMAND",
            "value": "/documents"
        }
    ],
    "route": {
        "actions": [
            {
                "id": "e2e1b674-dfbc-4c55-abf8-2055d07bbf5b",
                "nextActionId": null,
                "name": "Уточннение документа",
                "text": "Что вы хотите оформить?",
                "isMarkdownText": false,
                "buttons": [
                    {
                        "name": "Пропуск",
                        "nextActionId": "a8ea2a20-2b19-4378-8367-4f01b8bbdb91"
                    },
                    {
                        "name": "Отпуск",
                        "nextActionId": "cf8f62f9-f883-4873-b395-c55f2ec0aa74"
                    },
                ]
            },
            {
                "id": "a8ea2a20-2b19-4378-8367-4f01b8bbdb91",
                "nextActionId": "3e7f650d-1c13-4a39-bb56-188304222fb7",
                "name": "Шаблон пропуска",
                "fileId": "cfac4184-b04a-41dd-bbb3-4d64ae85d1f6",
                "fileName": "Шаблон_пропуска.txt"
            },
            {
                "id": "cf8f62f9-f883-4873-b395-c55f2ec0aa74",
                "nextActionId": "3e7f650d-1c13-4a39-bb56-188304222fb7",
                "name": "Шаблон отпуска",
                "fileId": "a73d0d55-2219-4002-9368-5fca53390d2a",
                "fileName": "Шаблон_отпуска.txt"
            },
            {
                "id": "3e7f650d-1c13-4a39-bb56-188304222fb7",
                "nextActionId": null,
                "name": "Запустить сценарий - Главное меню",
                "nextScenariosName": "main-scenarios",
                "startFromBegin": false
            }
        ]
    }
}