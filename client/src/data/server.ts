const serverURL: string = 'http://localhost:8080/';

async function registerUserOnServer(username: string, email: string, password: string) {

    let connectionError = false;
    let response;
    try {
        response = await fetch(`${serverURL}users/register`, {
            "method": 'POST',
            "headers": {
                'Content-Type': 'application/json'
            },
            "body": JSON.stringify({
                "username": username,
                "email": email,
                "password": password
            })
        });
    } catch (err) {
        connectionError = true;
    } 

    if (connectionError) {
        throw new Error('Server connection error');
    }

    if (!response.ok) {
        throw new Error('Username or email already used');
    }

    return response.json();
}

async function loginUserOnServer(usernameOrEmail: string, password: string) {

    const response = await fetch(`${serverURL}users/login`, {
        "method": 'POST',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "usernameOrEmail": usernameOrEmail,
            "password": password
        })
    });

    if (!response.ok) {
        throw new Error('Incorrect username or password');
    }

    return response.json();
}

async function addUserTodo(username: string, 
                           title: string, 
                           description: string, 
                           dueDate: string, 
                           status: string, 
                           priority: string) {

    const response = await fetch(`${serverURL}todos/add`, {
        "method": 'POST',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "title": title,
            "description": description,
            "username": username,
            "dueDate": dueDate,
            "status": status,
            "priority": priority
        })
    });

    if (!response.ok) {
        throw new Error('User does not exist!');
    }

    return response.json();
}

async function getAllUserTodo(username: string) {

    const response = await fetch(`${serverURL}todos/all?username=${username}`, {
        "method": 'GET',
        "headers": {
            'Content-Type': 'application/json'
        },
    });

    if (!response.ok) {
        throw new Error('User does not exist!');
    }

    return response.json();
}

async function editUserTodo(title: string, 
                            description: string, 
                            todoId: number, 
                            dueDate: string, 
                            status: string,
                            priority: string) {

    const response = await fetch(`${serverURL}todos/edit?id=${todoId}`, {
        "method": 'PATCH',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "title": title,
            "description": description,
            "dueDate": dueDate,
            "status": status,
            "priority": priority
        })
    });

    if (!response.ok) {
        throw new Error('Todo does not exist!');
    }

    return response.json();
}

async function todoDelete(todoId: number) {

    const response = await fetch(`${serverURL}todos/delete?id=${todoId}`, {
        "method": 'DELETE',
        "headers": {
            'Content-Type': 'application/json'
        }
    });

    if (response.status >= 200 && response.status < 300) {
        return;
    } else if (response.status == 404) {
        throw new Error('Todo not found');
    }
}

async function searchTodo(text: string, username: string) {

    const response = await fetch(`${serverURL}todos/search?text=${text}&username=${username}`, {
        "method": 'GET',
        "headers": {
            'Content-Type': 'application/json'
        },
    });

    if (!response.ok) {
        throw new Error('User does not exist!');
    }

    return response.json();
}

async function loadAllGroups() {

    const response = await fetch(`${serverURL}groups/all`, {
        "method": 'GET',
        "headers": {
            'Content-Type': 'application/json'
        },
    });

    return response.json();
}

async function addGroup(groupName: string, username: string) {

    const response = await fetch(`${serverURL}groups/add?username=${username}`, {
        "method": 'POST',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "groupName": groupName
        })
    });

    if (!response.ok) {
        throw new Error('Group already exists!');
    }

    return response.json();
}

async function loadMessages(groupName: string) {
    const response = await fetch(`${serverURL}messages/all?group=${groupName}`, {
        "method": 'GET',
        "headers": {
            'Content-Type': 'application/json'
        },
    });

    return response.json();
}

async function addMessage(groupName: string, username: string, message: string) {

    const response = await fetch(`${serverURL}messages/add`, {
        "method": 'POST',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "groupName": groupName,
            "message": message,
            "username": username,
        })
    });

    return response.json();
}

async function addUserToGroup(groupName: string, username: string) {

    const response = await fetch(`${serverURL}groups/join?username=${username}&groupName=${groupName}`, {
        "method": 'POST',
        "headers": {
            'Content-Type': 'application/json'
        },
    });

    return response.json();
}

async function removeUserFromGroup(groupName: string, username: string) {

    const response = await fetch(`${serverURL}groups/remove?username=${username}&groupName=${groupName}`, {
        "method": 'DELETE',
        "headers": {
            'Content-Type': 'application/json'
        },
    });
}

async function getTodoById(todoId: number) {

    const response = await fetch(`${serverURL}todos/get?id=${todoId}`, {
        "method": 'GET',
        "headers": {
            'Content-Type': 'application/json'
        },
    });

    return response.json();
}

async function updateUser(oldUsername: string, newUsername: string, email: string) {

    const response = await fetch(`${serverURL}users/update?username=${oldUsername}`, {
        "method": 'PATCH',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "username": newUsername,
            "email": email
        })
    })

    const json = await response.json()
    if (!response.ok) {
        throw new Error(json.message)
    }

    return json
}

function saveUserData(username: string, email: string) {
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
}

function clearUserData() {
    localStorage.clear();
}

export {
    addGroup, addMessage, addUserToGroup, addUserTodo,
    clearUserData,
    editUserTodo,
    getAllUserTodo, getTodoById, loadAllGroups, loadMessages, loginUserOnServer,
    registerUserOnServer, removeUserFromGroup, saveUserData,
    searchTodo,
    todoDelete, updateUser
};

