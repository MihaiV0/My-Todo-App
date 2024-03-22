<script setup lang="ts">
import { RouterView } from 'vue-router'
import NavButton from '@/components/NavButton.vue';
import NavSeparator from '@/components/NavSeparator.vue';
import { ref, computed, onMounted } from 'vue';
import { useRoute } from "vue-router";
import router from '@/router';
import { clearUserData } from '@/data/server';

const showNavbar = computed(() => {
    const currentRoute = useRoute();
    let value = true;
    if (
      currentRoute.path == "/" ||
      currentRoute.path == "/register" ||
      currentRoute.path == "/login" ||
      currentRoute.path == "/register-success"
    ) {
      value = false;
    }

    return value;
});

router.beforeEach((to, from) => {
    if (from.path == '/login' && to.path == '/my-todos') {
        username.value = localStorage.getItem('username') || 'Username not found';
    }

    if ((to.path == '/my-todos' || 
        to.path == '/my-profile' || 
        to.path == '/groups') && 
        !localStorage.getItem('username')
    ) {
      router.push(from.path);
    }
});

const currentRoute = computed(() => {
    return useRoute().path;
});

const username = ref('');

function logout() {
    clearUserData();
    router.push('/login');
}

</script>

<template>
    <div id="main-page">
        <nav 
            id="navbar"
            v-show="showNavbar"
        >
            <NavButton
                text="My todos"
                @button-click="$router.push('/my-todos')"
                :highlighted="currentRoute == '/my-todos'"
                :show-todo-icon="true"
            />
            <NavSeparator />
            <NavButton
                text="Groups"
                @button-click="$router.push('/groups')"
                :highlighted="currentRoute == '/groups'"
                :show-groups-icon="true"
            />
            <NavSeparator />
            <NavButton
                :text="username"
                @button-click="$router.push('/my-profile')"
                :highlighted="currentRoute == '/my-profile'"
                :show-profile-icon="true"
            />
            <NavSeparator />
            <NavButton
                text="Logout"
                @button-click="logout"
                :show-logout-icon="true"
            />
        </nav>
        <RouterView />
    </div>
</template>

<style scoped>
    #navbar {
        background-color: var(--green-secondary-color);
        height: var(--navbar-height);
        display: flex;
        justify-content: center;
        border-bottom: var(--navbar-border-bottom-height) solid var(--main-color);
    }
</style>
