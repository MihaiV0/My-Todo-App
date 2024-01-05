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
    }
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

</script>

<template>
    <select 
        :id="dropDownId"
        v-model="currentValue"
        class="drop-down"
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

</style>