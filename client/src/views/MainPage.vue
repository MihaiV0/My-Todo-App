<script setup lang="ts">
import NavButton from '@/components/NavButton.vue';
import NavSeparator from '@/components/NavSeparator.vue';

</script>

<template>
    <div id="main-page">
        <nav id="navbar">
            <NavButton
                text="My todos"
            />
            <NavSeparator />
            <NavButton
                text="Projects"
            />
            <NavSeparator />
            <NavButton
                text="My profile"
            />
        </nav>
        <div id="main-container">
            This is the main page
        </div>
    </div>
</template>

<style scoped>
    #navbar {
        background-color: var(--green-secondary-color);
        height: var(--navbar-height);
        display: flex;
        justify-content: center;
    }

    
</style>