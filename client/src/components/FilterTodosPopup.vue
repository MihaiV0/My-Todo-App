<script setup lang="ts">
import { ref, watch } from 'vue';
import CustomButton from './CustomButton.vue';
import CustomCheckbox from './CustomCheckbox.vue';
import PopupBase from './PopupBase.vue';

const mProps = defineProps({
    showPopup: Boolean,
    filterPrio1s: Boolean,
    filterPrio2s: Boolean,
    filterOpen: Boolean,
    filterInProgress: Boolean,
    filterClosed: Boolean,
});

const mEmits = defineEmits(['cancel', 'filter-todos']);

const mFilterPrio1s = ref(false);
const mFilterPrio2s = ref(false);
const mFilterOpen = ref(false);
const mFilterInProgress = ref(false);
const mFilterClosed = ref(false);

function prio1Changed(newValue: boolean) {
    mFilterPrio1s.value = newValue;
    if (mFilterPrio1s.value) {
        mFilterPrio2s.value = false;
    }
}

function prio2Changed(newValue: boolean) {
    mFilterPrio2s.value = newValue;
    if (mFilterPrio2s.value) {
        mFilterPrio1s.value = false;
    }
}

function openChanged(newValue: boolean) {
    mFilterOpen.value = newValue;
    if (mFilterOpen.value) {
        mFilterInProgress.value = false;
        mFilterClosed.value = false;
    }
}

function inProgressChanged(newValue: boolean) {
    mFilterInProgress.value = newValue;
    if (mFilterInProgress.value) {
        mFilterOpen.value = false;
        mFilterClosed.value = false;
    }
}

function closedChanged(newValue: boolean) {
    mFilterClosed.value = newValue;
    if (mFilterClosed.value) {
        mFilterInProgress.value = false;
        mFilterOpen.value = false;
    }
}

function closePopupAndTriggerFilter() {
    mEmits('filter-todos', mFilterPrio1s.value, 
        mFilterPrio2s.value, mFilterOpen.value, mFilterInProgress.value, mFilterClosed.value);
}

watch(
    () => mProps.filterPrio1s,
    (oldValue, newValue) => {
        mFilterPrio1s.value = mProps.filterPrio1s;
    }
);

watch(
    () => mProps.filterPrio2s,
    (oldValue, newValue) => {
        mFilterPrio2s.value = mProps.filterPrio2s;
    }
);

watch(
    () => mProps.filterOpen,
    (oldValue, newValue) => {
        mFilterOpen.value = mProps.filterOpen;
    }
);

watch(
    () => mProps.filterInProgress,
    (oldValue, newValue) => {
        mFilterInProgress.value = mProps.filterInProgress;
    }
);

watch(
    () => mProps.filterClosed,
    (oldValue, newValue) => {
        mFilterClosed.value = mProps.filterClosed;
    }
);

</script>

<template>
    <PopupBase 
        :show="showPopup" 
        @close-popup="mEmits('cancel')"
    >
        <div class="popup-body">
            <h2>
                Filtering options
            </h2>
            <div id="options-container">
                <div class="prio-group">
                    <CustomCheckbox 
                        name="prio-filter"
                        id="prio-1-filter"
                        text="Only prio 1s"
                        @value-changed="prio1Changed"
                        :initial-value="mFilterPrio1s"
                    />
                    <CustomCheckbox 
                        name="prio-filter"
                        id="prio-2-filter"
                        text="Only prio 2s"
                        @value-changed="prio2Changed"
                        :initial-value="mFilterPrio2s"
                    />
                </div>
                <div class="status-group">
                    <CustomCheckbox 
                        name="status-filter"
                        id="open-filter"
                        text="Only open"
                        @value-changed="openChanged"
                        :initial-value="mFilterOpen"
                    />
                    <CustomCheckbox 
                        name="status-filter"
                        id="in-progress-filter"
                        text="Only in progress"
                        @value-changed="inProgressChanged"
                        :initial-value="mFilterInProgress"
                    />
                    <CustomCheckbox 
                        name="status-filter"
                        id="closed-filter"
                        text="Only closed"
                        @value-changed="closedChanged"
                        :initial-value="mFilterClosed"
                    />
                </div>
            </div>
            <div id="controls-container">
                <CustomButton 
                    text="OK" 
                    @button-click="closePopupAndTriggerFilter" 
                />
                <button @click="mEmits('cancel')">
                    Cancel
                </button>
            </div>
        </div>
    </PopupBase>
</template>

<style scoped>
    .popup-body {
        background-color: white;
        width: 20vw;
        height: 48vh;
        display: flex;
        flex-direction: column;
        justify-content: flex-start;
        align-items: center;
        padding: 10px;
        border-radius: 10px;
        gap: 5vh;
    }

    .prio-group {
        width: 10vw;
        position: relative;
        border: 0.2px solid var(--main-color);
        border-radius: 10px;
        padding: 2vh;
    }

    .prio-group::before {
        content: "Prio filtering";
        position: absolute;
        top: -1.5vh;
        background-color: #fff;
        padding: 0 5px;
    }

    .status-group {
        width: 10vw;
        position: relative;
        border: 0.2px solid var(--main-color);
        border-radius: 10px;
        padding: 2vh;
    }

    .status-group::before {
        content: "Status filtering";
        position: absolute;
        top: -1.5vh;
        background-color: #fff;
        padding: 0 5px;
    }

    #controls-container {
        display: flex;
        justify-content: center;
        gap: 2vw;
    }
    h2 {
        margin-bottom: 0;
        margin-top: 2vh;
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

    #options-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 3vh;
    }

</style>