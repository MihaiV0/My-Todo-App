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

interface Todo {
    todoId: number;
    title: string;
    description: string;
    dueDate: string;
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
                todos.value.push(todo);
            });
        })
        .catch(err => {
            errorMessage.value = err.message;
            showErrorPopup.value = true;
        });
}

function updateTodosFromServerAndSort() {
    getAllUserTodo(localStorage.getItem('username') || '')
        .then((res: Array<Todo>) => {
            todos.value = [];
            res.forEach(todo => {
                todos.value.push(todo);
            });
            sortTodos();
        })
        .catch(err => {
            errorMessage.value = err.message;
            showErrorPopup.value = true;
        });
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

function showTodoDesciption(todoId: number) {
    const todo = todos.value.find(todo => todo.todoId == todoId);

    activeTodoId.value = todoId;
    todoDescription.value = todo?.description || '';
    todoTitle.value = todo?.title || '';
    dueDate.value = useDateFormat(todo?.dueDate, 'DD.MM.YYYY').value;
}

function addNewTodo() {
    if (currentOperation.value != 'edit' && currentOperation.value != 'add') {
        todoTitle.value = 'New Todo';
        todoDescription.value = '';
        dueDate.value = useDateFormat(useNow(), 'DD.MM.YYYY').value;
        activeTodoId.value = -1;

        todos.value.push({
            todoId: -1,
            title: 'New Todo',
            description: '',
            dueDate: useDateFormat(useNow(), 'DD.MM.YYYY').value
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
        }
    }
}

function saveTodo() {
    if (currentOperation.value == 'add') {
        if (todoTitle.value && todoDescription.value && dueDate.value) {
            addUserTodo(localStorage.getItem('username') || '', 
                        todoTitle.value, 
                        todoDescription.value, 
                        dueDate.value)
                .then((res: Todo) => {
                    todos.value.pop();
                    todos.value.push(res);
                    searchTodoByTitleAndSort();

                    todoTitle.value = '';
                    todoDescription.value = '';
                    activeTodoId.value = -2;
                    dueDate.value = '';
                    currentOperation.value = '';
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
            editUserTodo(todoTitle.value, todoDescription.value, activeTodoId.value, dueDate.value)
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
        updateTodosFromServerAndSort();
    } else {
        searchTodo(searchText.value, localStorage.getItem('username') || '')
            .then((res: Array<Todo>) => {
                todos.value = res;
                if (sorting.value != 'No sorting') {
                    sortTodos();
                }
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
        case 'No sorting': updateTodosFromServer();
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
                Sorting:
                <DropDown 
                    drop-down-id="sort-by-drop-down"
                    :values="sortingOptions"
                    initial-value="No sorting"
                    @value-changed="updateSortingAndSortTodos"
                />
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
                @title-changed="titleChanged"
                @description-changed="descriptionChanged"
                @date-changed="dateChanged"
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
        font-size: inherit;
    }

    .blue-sign {
        color: #00129b;
        font-size: inherit;
    }

    .red-sign {
        color: #C30000;
        font-size: inherit;
    }

    #sorting-tools {
        height: 100%;
        display: flex;
        align-items: center;
        padding: 0 1vw;
    }

</style>