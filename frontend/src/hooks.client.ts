import {type Handle} from "@sveltejs/kit";

export const handle: Handle = async ({ event, resolve }) => {
	console.log(event);
	
	if (event.route.id?.startsWith("/scenaries/[scenarios]/documents")) {
		event.locals.localRequest = true;
	}

	return resolve(event);
};