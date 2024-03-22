<script setup lang="ts">
import TodoDescription from '@/components/TodoDescription.vue';
import TodoPanel from '@/components/TodoPanel.vue';
import ToolbarBase from '@/components/ToolbarBase.vue';
import ToolbarButtonBase from '@/components/ToolbarButtonBase.vue';
import ToolbarSeparator from '@/components/ToolbarSeparator.vue';
import { ref, watch, onMounted } from 'vue';
import { addUserTodo, editUserTodo, getAllUserTodo, searchTodo, todoDelete } from '@/data/server';
import ErrorMessagePopup from '@/components/ErrorMessagePopup.vue';
import DeleteTodoPopup from '@/components/DeleteTodoPopup.vue';
import CustomInput from '@/components/CustomInput.vue';
import { useDateFormat, useNow } from '@vueuse/core';
import DropDown from '@/components/DropDown.vue';
import FilterTodosPopup from '@/components/FilterTodosPopup.vue';

interface Todo {
    todoId: number;
    title: string;
    description: string;
    dueDate: string;
    status: string;
    priority: string;
}

const todos = ref<Array<Todo>>([]);

onMounted(() => {
    updateTodosFromServer();
});

const sortingOptions = ["No sorting", "Due date", "Due date desc"];

function updateTodosFromServer() {
    getAllUserTodo(localStorage.getItem('username') || '')
        .then((res: Array<Todo>) => {
            todos.value = [];
            res.forEach(todo => {
                replaceUnderscoresWithSpaces(todo);
                todos.value.push(todo);
            });
        })
        .catch(err => {
            errorMessage.value = err.message;
            showErrorPopup.value = true;
        });
}

function updateTodosFromServerAndSortFilter() {
    getAllUserTodo(localStorage.getItem('username') || '')
        .then((res: Array<Todo>) => {
            todos.value = [];
            res.forEach(todo => {
                replaceUnderscoresWithSpaces(todo);
                todos.value.push(todo);
            });
            sortTodos();
            filterTodos();
        })
        .catch(err => {
            errorMessage.value = err.message;
            showErrorPopup.value = true;
        });
}

function replaceUnderscoresWithSpaces(todo: Todo) {
    todo.status = todo.status.replace(/_/g, ' ');
    todo.priority = todo.priority.replace(/_/g, ' ');
}

const todoDescription = ref('');
const todoTitle = ref('');
const activeTodoId = ref(-2);
const editing = ref(false);
const currentOperation = ref('');
const errorMessage = ref('');
const showErrorPopup = ref(false);
const editingTodoId = ref(0);
const showDeletePopup = ref(false);
const dueDate = ref('');
const sorting = ref('No sorting');
const searchText = ref('');
const status = ref('');
const priority = ref('');
const mShowFilterTodosPopup = ref(false);
const mFilterPrio1s = ref(false);
const mFilterPrio2s = ref(false);
const mFilterOpen = ref(false);
const mFilterInProgress = ref(false);
const mFilterClosed = ref(false);
const mFiltersActive = ref(false);

function showTodoDesciption(todoId: number) {
    const todo = todos.value.find(todo => todo.todoId == todoId);

    activeTodoId.value = todoId;
    todoDescription.value = todo?.description || '';
    todoTitle.value = todo?.title || '';
    dueDate.value = useDateFormat(todo?.dueDate, 'DD.MM.YYYY').value;
    status.value = todo?.status || '';
    priority.value = todo?.priority || '';
}

function addNewTodo() {
    if (currentOperation.value != 'edit' && currentOperation.value != 'add') {
        todoTitle.value = 'New Todo';
        todoDescription.value = '';
        dueDate.value = useDateFormat(useNow(), 'DD.MM.YYYY').value;
        activeTodoId.value = -1;
        status.value = "OPEN";
        priority.value = "PRIO 2";

        todos.value.push({
            todoId: -1,
            title: 'New Todo',
            description: '',
            dueDate: useDateFormat(useNow(), 'DD.MM.YYYY').value,
            status: "OPEN",
            priority: "PRIO 2"
        });

        currentOperation.value = 'add';
    } else {
        errorMessage.value = 'Already adding or editing a todo!';
        showErrorPopup.value = true;
    }
}

function cancel() {
    if (currentOperation.value == 'add') {
        todos.value.pop();

        todoTitle.value = '';
        todoDescription.value = '';
        dueDate.value = '';
        activeTodoId.value = -2;
        status.value = '';
        priority.value = '';

        currentOperation.value = '';
    } else if (currentOperation.value == 'edit') {
        editingTodoId.value = 0;
        currentOperation.value = '';
        editing.value = false;

        const activeTodo = todos.value.find(todo => todo.todoId == activeTodoId.value);
        if (activeTodo) {
            todoTitle.value = activeTodo.title;
            todoDescription.value = activeTodo.description;
            dueDate.value = useDateFormat(activeTodo.dueDate, 'DD.MM.YYYY').value;
            status.value = activeTodo.status;
            priority.value = activeTodo.priority;
        }
    }
}

function saveTodo() {
    if (currentOperation.value == 'add') {
        if (todoTitle.value && todoDescription.value && dueDate.value) {
            addUserTodo(localStorage.getItem('username') || '', todoTitle.value, todoDescription.value, 
                        dueDate.value, status.value, priority.value)
                .then((res: Todo) => {
                    todos.value.pop();
                    todos.value.push(res);
                    searchTodoByTitleAndSort();

                    todoTitle.value = '';
                    todoDescription.value = '';
                    activeTodoId.value = -2;
                    dueDate.value = '';
                    currentOperation.value = '';
                    status.value = '';
                    priority.value = '';
                })
                .catch(err => {
                    errorMessage.value = err.message;
                    showErrorPopup.value = true;
                });
        } else {
            errorMessage.value = 'All fields are mandatory';
            showErrorPopup.value = true;
        }
    } else if (currentOperation.value == 'edit') {
        if (todoTitle.value && todoDescription.value && dueDate.value) {
            editUserTodo(todoTitle.value, todoDescription.value, activeTodoId.value, 
                         dueDate.value, status.value, priority.value)
                .then((res: Todo) => {
                    const index = todos.value.findIndex(todo => todo.todoId == res.todoId);
                    if (index != -1) {
                        todos.value[index] = res;

                        todoTitle.value = res.title;
                        todoDescription.value = res.description;
                        activeTodoId.value = res.todoId;
                        currentOperation.value = '';
                        editing.value = false;
                        editingTodoId.value = 0;

                        searchTodoByTitleAndSort();
                    } else {
                        errorMessage.value = 'Todo index is -1 - not found in array!';
                        showErrorPopup.value = true;
                    }
                })
                .catch(err => {
                    errorMessage.value = err.message;
                    showErrorPopup.value = true;
                });
        } else {
            let emptyFieldName = '';
            if (!todoTitle.value) {
                emptyFieldName = 'title';
            } else if (!todoDescription.value) {
                emptyFieldName = 'description';
            } else if (!dueDate.value) {
                emptyFieldName = 'due date'
            }

            errorMessage.value = `Field ${emptyFieldName} cannot be empty!`;
            showErrorPopup.value = true;
        }
    }
}

function titleChanged(newTitle: string) {
    todoTitle.value = newTitle;
}

function descriptionChanged(newDescription: string) {
    todoDescription.value = newDescription;
}

watch(
    activeTodoId, 
    (oldValue, newValue) => {
        editing.value = (activeTodoId.value == -1 && currentOperation.value == 'add') || 
            (activeTodoId.value == editingTodoId.value && currentOperation.value == 'edit');
    }
);

function closeErrorPopup() {
    showErrorPopup.value = false;
    errorMessage.value = '';
}

function editTodo() {
    if (currentOperation.value != 'edit' && currentOperation.value != 'add') {
        currentOperation.value = 'edit';
        editingTodoId.value = activeTodoId.value;
        editing.value = true;
    } else {
        errorMessage.value = 'Already adding or editing a todo!';
        showErrorPopup.value = true;
    }
}

function deleteTodo() {
    showDeletePopup.value = true;
}

function cancelDeleteTodo() {
    showDeletePopup.value = false;
}

function actionDeleteTodo() {
    todoDelete(activeTodoId.value)
        .then(res => {
            showDeletePopup.value = false;
            todoTitle.value = '';
            todoDescription.value = '';
            activeTodoId.value = -2;

            updateTodosFromServer();
            searchTodoByTitleAndSort();
        })
        .catch(err => {
            showDeletePopup.value = false;
            errorMessage.value = err.message;
            showErrorPopup.value = true;
        });
}

function search(newSearchText: string) {
    searchText.value = newSearchText;
    searchTodoByTitleAndSort();
}

function searchTodoByTitleAndSort() {
    if (!searchText.value) {
        updateTodosFromServerAndSortFilter();
    } else {
        searchTodo(searchText.value, localStorage.getItem('username') || '')
            .then((res: Array<Todo>) => {
                todos.value = res;
                if (sorting.value != 'No sorting') {
                    sortTodos();
                }
                filterTodos();
            });
    }
}

function dateChanged(newDate: string) {
    dueDate.value = newDate;
}

function updateSortingAndSortTodos(newSorting: string) {
    sorting.value = newSorting;
    sortTodos();
}

function sortTodos() {
    switch(sorting.value) {
        case 'No sorting': 
            if (!mFiltersActive.value) {
                updateTodosFromServer();
            }
            break;
        case 'Due date': todos.value.sort((a: Todo, b: Todo) => 
            new Date(a.dueDate).getTime() - new Date(b.dueDate).getTime()
        );
            break;
        case 'Due date desc': todos.value.sort((a: Todo, b: Todo) => 
            new Date(b.dueDate).getTime() - new Date(a.dueDate).getTime()
        );
            break;
    }
}

function statusChanged(newStatus: string) {
    status.value = newStatus;
}

function priorityChanged(newPriority: string) {
    priority.value = newPriority;
}

function showFilterTodosPopup() {
    mShowFilterTodosPopup.value = true;
}

function hideFilterTodosPopup() {
    mShowFilterTodosPopup.value = false;
}

function filterTodos() {
    if (mFilterPrio1s.value) {
        todos.value = todos.value.filter(todo => todo.priority == 'PRIO 1');
    } else if (mFilterPrio2s.value) {
        todos.value = todos.value.filter(todo => todo.priority == 'PRIO 2');
    }
    if (mFilterOpen.value) {
        todos.value = todos.value.filter(todo => todo.status == 'OPEN');
    } else if (mFilterInProgress.value) {
        todos.value = todos.value.filter(todo => todo.status == 'IN PROGRESS');
    } else if (mFilterClosed.value) {
        todos.value = todos.value.filter(todo => todo.status == 'CLOSED');
    }
}

function filterTodosAction(filterPrio1s: boolean, 
                           filterPrio2s: boolean, 
                           filterOpen: boolean, 
                           filterInProgress: boolean, 
                           filterClosed: boolean) {
    mFilterPrio1s.value = filterPrio1s;
    mFilterPrio2s.value = filterPrio2s;
    mFilterOpen.value = filterOpen;
    mFilterInProgress.value = filterInProgress;
    mFilterClosed.value = filterClosed;
    checkFiltersActive();
    updateTodosFromServerAndSortFilter();
    hideFilterTodosPopup();
}

function checkFiltersActive() {
    if (mFilterPrio1s.value || mFilterPrio2s.value || mFilterOpen.value ||
        mFilterInProgress.value || mFilterClosed.value) {
        mFiltersActive.value = true;
    } else {
        mFiltersActive.value = false;
    }
}

</script>

<template>
    <div id="my-todos">
        <ToolbarBase>
            <ToolbarButtonBase @click="addNewTodo">
                <span class="material-symbols-outlined green-sign">
                    add_task
                </span>
                Add new todo
            </ToolbarButtonBase>
            <ToolbarSeparator v-show="activeTodoId > 0"/>
            <ToolbarButtonBase
                v-show="activeTodoId > 0"
                @click="editTodo"
            >
                <span class="material-symbols-outlined blue-sign">
                    edit
                </span>
                Edit
            </ToolbarButtonBase>
            <ToolbarSeparator v-show="activeTodoId > 0"/>
            <ToolbarButtonBase
                v-show="activeTodoId > 0"
                @click="deleteTodo"
            >
                <span class="material-symbols-outlined red-sign">
                    delete
                </span>
                Delete
            </ToolbarButtonBase>
            <ToolbarSeparator v-show="editing"/>
            <ToolbarButtonBase
                v-show="editing"
                @click="saveTodo"
            >
                <span class="material-symbols-outlined green-sign">
                    check
                </span>
                Save
            </ToolbarButtonBase>
            <ToolbarSeparator v-show="editing"/>
            <ToolbarButtonBase
                v-show="editing"
                @click="cancel"
            >
                <span class="material-symbols-outlined red-sign">
                    close
                </span>
                Cancel
            </ToolbarButtonBase>
            <ToolbarSeparator />
            <CustomInput 
                input-type="text"
                placeholder-text="Search by title"
                :is-password-field="false"
                @change-value="search"
            />
            <ToolbarSeparator />
            <div id="sorting-tools">
                Sort by:
                <DropDown 
                    drop-down-id="sort-by-drop-down"
                    :values="sortingOptions"
                    initial-value="No sorting"
                    @value-changed="updateSortingAndSortTodos"
                    :enabled="true"
                />
            </div>
            <ToolbarSeparator />
            <ToolbarButtonBase @click="showFilterTodosPopup">
                Filtering options
            </ToolbarButtonBase>
            <ToolbarSeparator />
            <div id="filter-status">
                Filters: {{ mFiltersActive ? 'on' : 'off' }}
            </div>
        </ToolbarBase>
        <div id="my-todos-container">
            <TodoPanel 
                :todos="todos"
                @show-todo-description="showTodoDesciption"
                :active-todo-id="activeTodoId"
            />
            <TodoDescription 
                :description="todoDescription"
                :title="todoTitle"
                :dueDate="dueDate"
                :editing="editing"
                :active-todo-id="activeTodoId"
                :status="status"
                :priority="priority"
                @title-changed="titleChanged"
                @description-changed="descriptionChanged"
                @date-changed="dateChanged"
                @status-changed="statusChanged"
                @priority-changed="priorityChanged"
            />
        </div>
    </div>
    <Teleport to="body">
        <ErrorMessagePopup
            :error-text="errorMessage"
            :show-popup="showErrorPopup"
            @close="closeErrorPopup"
        />
        <DeleteTodoPopup 
            :show-popup="showDeletePopup"
            @cancel="cancelDeleteTodo"
            @delete-todo="actionDeleteTodo"
        />
        <FilterTodosPopup
            :show-popup="mShowFilterTodosPopup"
            @cancel="hideFilterTodosPopup"
            @filter-todos="filterTodosAction"
            :filter-prio1s="mFilterPrio1s"
            :filter-prio2s="mFilterPrio2s"
            :filter-open="mFilterOpen"
            :filter-in-progress="mFilterInProgress"
            :filter-closed="mFilterClosed"
        />
    </Teleport>
</template>

<style scoped>
    #my-todos-container {
        display: flex;
        height: calc(100vh - var(--navbar-height) - var(--navbar-border-bottom-height) - var(--toolbar-height) 
            - var(--toolbar-border-bottom-height));
    }

    #my-todos {
        height: calc(100vh - var(--navbar-height) - var(--navbar-border-bottom-height));
    }

    .green-sign {
        color: #007900;
    }

    .blue-sign {
        color: #00129b;
    }

    .red-sign {
        color: #C30000;
    }

    #sorting-tools {
        height: 100%;
        display: flex;
        align-items: center;
        padding: 0 1vw;
    }

    #filter-status {
        margin: 0 1vw;
        display: flex;
        align-items: center;
    }

</style>