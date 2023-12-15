<script setup lang="ts">
import CustomButton from './CustomButton.vue';
import PopupBase from './PopupBase.vue';

const props = defineProps({
    errorText: String,
    showPopup: Boolean,
});

const emits = defineEmits(['close']);

</script>

<template>
    <PopupBase 
        :show="showPopup" 
        @close-popup="emits('close')"
    >
        <div class="popup-body">
            <div>
                <span class="material-symbols-outlined">
                    error
                </span>
            </div>
            <h2>
                {{ errorText == 'Failed to fetch' ? 'Server connection error' : errorText }}
            </h2>
            <CustomButton 
                text="OK" 
                @button-click="emits('close')" 
            />
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
</style>