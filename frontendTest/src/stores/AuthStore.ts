import {defineStore} from 'pinia';
import {useRouter} from 'vue-router';
import {ref} from 'vue';
import {useClient} from "~/plugins/client";
import {authenticateUser, getUserInfo, logoutUser, refreshToken as refreshTokenRequest} from "~/client";


export interface User {
    id: string,
    email: string,
    username: string,
    roles: string[]
}

export const useAuthStore = defineStore('auth', () => {
    const router = useRouter();
    const client = useClient();

    const accessToken = ref<string | null>(null);
    const refreshToken = ref<string | null>(null);
    const isAuthenticated = ref<boolean>(false);

    const userInfo = ref<User | null>(null);

    onMounted(async () => {
        if(accessToken.value && refreshToken.value) {
            await fetchUserProfile();
        }
    })

    async function login(email: string, password: string) {
        try {
            const response = await authenticateUser({
                composable: '$fetch',
                client,
                body: {
                    email,
                    password,
                }
            })

            if(response?.data == null || response?.code != 200) {
                throw Error("Login failed")
            }

            accessToken.value = response.data.accessToken;
            refreshToken.value = response.data.refreshToken;
            isAuthenticated.value = true;

            await fetchUserProfile();

            // 로그인 후 홈 화면으로 이동
            await router.push({name: 'index'});
        } catch (error) {
            console.error('Login failed:', error);
            throw new Error('로그인 실패: 잘못된 자격 증명');
        }
    }

    async function logout() {
        try {
            await logoutUser({
                composable: '$fetch',
                body: {
                    accessToken: accessToken.value ?? "",
                    refreshToken: refreshToken.value ?? "",
                }
            })
            accessToken.value = null;
            refreshToken.value = null;
            isAuthenticated.value = false;

            // 로그아웃 후 로그인 페이지로 이동
            await router.push({name: 'login'});
        } catch (error) {
            console.error('Logout failed:', error);
            throw new Error('로그아웃 실패');
        }
    }

    async function refreshAccessToken() {
        try {
            if (!refreshToken.value) {
                throw new Error('Refresh token is missing. Please log in again.');
            }

            const response = await refreshTokenRequest({
                composable: '$fetch',
                body: {
                    refreshToken: refreshToken.value,
                }
            })

            if(response.data == null || response.code != 200) {
                throw Error("Refresh failed")
            }

            accessToken.value = response.data.accessToken;
            refreshToken.value = response.data.refreshToken;
        } catch (error) {
            console.error('Failed to refresh token:', error);
            await logout(); // refreshToken 갱신 실패 시 로그아웃 처리
        }
    }


    async function fetchUserProfile() {
        try {
            const response = await getUserInfo({
                composable: '$fetch',
                auth: () => accessToken.value ?? ""
            });

            if(response.data == null || response.code != 200) {
                throw Error("Refresh failed")
            }

            userInfo.value = response.data;
            isAuthenticated.value = true;
        } catch (error) {
            console.error('Failed to fetch user profile:', error);
            throw new Error('사용자 정보를 가져오는 데 실패했습니다.');
        }
    }

    return {
        accessToken,
        refreshToken,
        isAuthenticated,
        login,
        fetchUserProfile,
        userInfo,
        logout,
        refreshAccessToken,
    };
}, {
    persist: {
        pick: ['accessToken', 'refreshToken']
    }
});