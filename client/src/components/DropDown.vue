<script setup lang="ts">
import { onMounted, ref, watch } from 'vue';

const props = defineProps({
    values: {
        type: Array,
        required: true,
        default: () => [],
        validator: (value) => {
            return Array.isArray(value) && value.every((item) => typeof item === 'string');
        },
    },
    initialValue: String,
    dropDownId: {
        type: String,
        required: true
    },
    enabled: {
        type: Boolean,
        required: true
    },
    customSize: Boolean,
});

const emits = defineEmits(['value-changed']);

const currentValue = ref('');

onMounted(() => {
    currentValue.value = props.initialValue || '';
});

watch(
    currentValue,
    (oldValue, newValue) => {
        emits('value-changed', currentValue.value);
    }
)

watch(
    () => props.initialValue,
    (oldValue, newValue) => {
        currentValue.value = props.initialValue || '';
    }
)

</script>

<template>
    <select 
        :id="dropDownId"
        v-model="currentValue"
        class="drop-down"
        :disabled="!enabled"
        :style="{
            fontSize: customSize ? '14px' : '',
            width: customSize ? '18vw' : '',
        }"
    >
        <option 
            v-for="element in values"
            :value="element"
            v-html="element"
        >
        </option>
    </select>
</template>

<style scoped>
    .drop-down {
        padding: 0 7px;
        outline: none;
        height: var(--toolbar-height);
    }

    select option:checked {
        background-color: var(--main-color);
        color: white;
    }

    select:disabled {
        background-color: white;
        opacity: 0.7;
    }

</style>