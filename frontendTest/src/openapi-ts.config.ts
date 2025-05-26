import { defineConfig } from '@hey-api/openapi-ts';
import { config } from "dotenv"
import { resolve } from "path"

const path = resolve(__dirname, "../../.env")

config({ path })

export default defineConfig({
    input: `${process.env.ANTBIZ_HOST_NAME}/v3/api-docs`,
    output: {
        format: 'prettier',
        lint: 'eslint',
        path: './client'
    },
    plugins: [
        '@hey-api/schemas',
        '@hey-api/client-nuxt',
        {
            dates: true,
            name: '@hey-api/transformers'
        },
        {
            enums: 'typescript',
            name: '@hey-api/typescript'
        },
        {
            name: '@hey-api/sdk',
            transformer: true
        },
        {
            bundle: true,
            name: '@hey-api/client-nuxt',
        },
    ]
})