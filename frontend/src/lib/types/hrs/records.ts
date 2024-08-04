import {Number, Record, String} from "runtypes";

export const HrRecord = Record({
    chatId: Number,
    username: String,
    firstName: String.optional(),
    lastName: String.optional(),
});