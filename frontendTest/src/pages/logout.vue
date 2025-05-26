<script setup lang = "ts" >
import {useRouter} from 'vue-router';

const router = useRouter();
const authStore = useAuthStore();

const handleLogout = async() => {
  await authStore.logout();
  await router.push('/'); // Redirect to logout confirmation page
}

onMounted(async () => await handleLogout())
</script>
