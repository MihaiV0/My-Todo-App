<script setup lang="ts">
import { ref, watch } from 'vue';
import TooltipDisplay from './TooltipDisplay.vue';

const props = defineProps({
    placeholderText: String,
    inputType: String,
    isPasswordField: Boolean,
    tooltipText: String,
    initialValue: String,
    inputDisabled: Boolean,
    customWidth: String,
});

const emits = defineEmits(['change-value']);

const inputText = ref('');
const focusedDiv = ref(false);
const showTooltip = ref(false);
const mousePositionX = ref(0);
const mousePositionY = ref(0);

function blurInput() {
    if (props.tooltipText) {
        mousePositionX.value = 0;
        mousePositionY.value = 0;
        showTooltip.value = false;
    }

    if (props.isPasswordField) {
        focusedDiv.value = false;
    }
}

function mouseClick($event: MouseEvent) {
    if (props.tooltipText) {
        mousePositionX.value = $event.clientX;
        mousePositionY.value = $event.clientY;
        showTooltip.value = true;
    }

    if (props.isPasswordField) {
        focusedDiv.value = true;
    }
}

watch(
    () => props.initialValue, 
    (oldValue, newValue) => {
        inputText.value = props.initialValue || '';
    }
);

</script>

<template>
    <div 
        class="input-container"
        :class="{ 
            'password-input': isPasswordField,
            'focus-div': focusedDiv 
        }"
        :style="{ width: customWidth }"
    >
        <input 
            :type="inputType"
            :placeholder="placeholderText"
            :disabled="inputDisabled"
            v-model="inputText"
            
            :class="{ 
                'reduced-input': isPasswordField, 
                'normal-input-size': !isPasswordField,
                'normal-input': !isPasswordField 
            }"
            @keyup="emits('change-value', inputText)"
            @blur="blurInput"
            @click="mouseClick($event)"
        >
        <slot></slot>
        <TooltipDisplay
            :show-tooltip="showTooltip"
            :text="tooltipText"
            :top-position="`calc(${mousePositionY}px + 4vh)`"
            :left-position="`calc(${mousePositionX}px + 1.2vw)`"
        />
    </div>
</template>

<style scoped>
    input {
        font-size: var(--default-font-size);
        border: 0.5px solid transparent;
        padding: 5px 7px;
        outline: none; /* remove default outline */
    }

    input:disabled {
        background-color: white;
        opacity: 0.7;
    }

    .normal-input:hover {
        border: 0.5px solid var(--main-color);
    }

    .normal-input:focus {
        border: 0.5px solid var(--main-color);
    }

    .input-container {
        display: flex;
        flex-wrap: nowrap;
        width: 15.666666vw;
    }

    .password-input {
        border: 0.5px solid transparent;
    }

    .focus-div {
        border: 0.5px solid var(--main-color);
    }

    .password-input:hover {
        border: 0.5px solid var(--main-color);
    }

    .reduced-input {
        width: 82%;
    }

    .normal-input-size {
        width: 100%;
    }
</style>