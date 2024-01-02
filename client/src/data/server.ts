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

async function addUserTodo(username: string, title: string, description: string) {

    const response = await fetch(`${serverURL}todos/add`, {
        "method": 'POST',
        "headers": {
            'Content-Type': 'application/json'
        },
        "body": JSON.stringify({
            "title": title,
            "description": description,
            "username": username
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

function saveUserData(username: string, email: string) {
    localStorage.setItem('username', username);
    localStorage.setItem('email', email);
}

function clearUserData() {
    localStorage.clear();
}

export {
    addUserTodo,
    clearUserData,
    getAllUserTodo,
    loginUserOnServer,
    registerUserOnServer,
    saveUserData
};

