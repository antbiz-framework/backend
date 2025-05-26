import { createClient } from '@hey-api/client-nuxt';
import {defineNuxtPlugin} from '#app';

export const useClient = () => {
    const config = useRuntimeConfig();
    return createClient({
        baseURL: config.public.apiBaseUrl,

        onResponse: (response) => {
            if(response.response.status == 401 || response.response.status == 403) {

            }
        }
    });
};

export default defineNuxtPlugin(() => {
    return {
        provide: {
            client: useClient(),
        },
    };
});
