import {sveltekit} from '@sveltejs/kit/vite';
import {optimizeCss} from "carbon-preprocess-svelte";
import {defineConfig} from 'vite';

export default defineConfig({
	optimizeDeps: {
		exclude: ["carbon-components-svelte", "carbon-pictograms-svelte"],
	},
	plugins: [sveltekit(), optimizeCss()],
	server: {
		strictPort: true,
		watch: {
			usePolling: process.env.USE_POLLING,
		},
		hmr: {
			clientPort: 5173
		},
		host: '0.0.0.0',
		port: 5173,
	}
});
