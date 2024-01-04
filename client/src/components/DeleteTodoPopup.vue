<script setup lang="ts">
import CustomButton from './CustomButton.vue';
import PopupBase from './PopupBase.vue';

const props = defineProps({
    showPopup: Boolean,
});

const emits = defineEmits(['cancel', 'delete-todo']);

</script>

<template>
    <PopupBase 
        :show="showPopup" 
        @close-popup="emits('cancel')"
    >
        <div class="popup-body">
            <div>
                <span class="material-symbols-outlined">
                    error
                </span>
            </div>
            <h2>
                Are you sure you want to delete this todo?
            </h2>
            <div id="controls-container">
                <CustomButton 
                    text="OK" 
                    @button-click="emits('delete-todo')" 
                />
                <button @click="emits('cancel')">
                    Cancel
                </button>
            </div>
        </div>
    </PopupBase>
</template>

<style scoped>
    .popup-body {
        background-color: white;
        width: 45vw;
        height: 25vh;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        padding: 10px;
        border-radius: 10px;
    }

    .material-symbols-outlined {
        color: red;
        font-size: 50px;
    }

    #controls-container {
        display: flex;
        justify-content: center;
        gap: 2vw;
    }

    button {
        padding: 7px 14px;
        background-color: var(--green-secondary-color);
        color: white;
        border-color: var(--green-secondary-color);
        font-size: var(--default-font-size);
    }

    button:hover {
        border-color: currentColor;
        cursor: pointer;
    }
</style>