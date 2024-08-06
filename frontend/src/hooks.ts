import type {Reroute} from '@sveltejs/kit';

const replaceDefault: Record<string, string> = {
	'/': '/scenaries',
};

export const reroute: Reroute = ({ url }) => {    
	if (url.pathname in replaceDefault) {
		return replaceDefault[url.pathname];
	}
};