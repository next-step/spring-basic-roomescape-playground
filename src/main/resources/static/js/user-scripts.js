document.addEventListener('DOMContentLoaded', function () {
    updateUIBasedOnLogin();

    const logoutButton = document.getElementById('logout-btn');

    if (logoutButton) {
        logoutButton.addEventListener('click', function (event) {
            event.preventDefault();

            fetch('/logout', {
                method: 'POST',
                credentials: 'include'
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Logout failed');
                    }

                    window.location.href = '/';
                })
                .catch(error => {
                    console.error('Logout error:', error);
                });
        });
    }

    const navbarDropdown = document.getElementById('navbarDropdown');

    if (navbarDropdown) {
        navbarDropdown.addEventListener('click', function (event) {
            event.preventDefault();

            const dropdown = event.target.closest('.nav-item.dropdown');

            if (!dropdown) {
                return;
            }

            const dropdownMenu = dropdown.querySelector('.dropdown-menu');

            if (dropdownMenu) {
                dropdownMenu.classList.toggle('show');
            }
        });
    }
});


function updateUIBasedOnLogin() {
    fetch('/login/check', {
        credentials: 'include'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Not logged in');
            }

            return response.json();
        })
        .then(data => {
            const profileName = document.getElementById('profile-name');
            const dropdown = document.querySelector('.nav-item.dropdown');
            const loginLink =
                document.querySelector('.nav-item a[href="/login"]');

            if (profileName) {
                profileName.textContent = data.name;
            }

            if (dropdown) {
                dropdown.style.display = 'block';
            }

            if (loginLink && loginLink.parentElement) {
                loginLink.parentElement.style.display = 'none';
            }
        })
        .catch(error => {
            console.log('로그아웃 상태:', error.message);

            const profileName = document.getElementById('profile-name');
            const dropdown = document.querySelector('.nav-item.dropdown');
            const loginLink =
                document.querySelector('.nav-item a[href="/login"]');

            if (profileName) {
                profileName.textContent = 'Profile';
            }

            if (dropdown) {
                dropdown.style.display = 'none';
            }

            if (loginLink && loginLink.parentElement) {
                loginLink.parentElement.style.display = 'block';
            }
        });
}


function login() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    if (!email || !password) {
        alert('Please fill in all fields.');
        return;
    }

    fetch('/login', {
        method: 'POST',
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            email: email,
            password: password
        })
    })
        .then(response => {
            if (!response.ok) {
                alert('Login failed');
                throw new Error('Login failed');
            }

            window.location.href = '/';
        })
        .catch(error => {
            console.error('Error during login:', error);
        });
}


function signup() {
    window.location.href = '/signup';
}


function register(event) {
    event.preventDefault();

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const name = document.getElementById('name').value;

    if (!email || !password || !name) {
        alert('Please fill in all fields.');
        return;
    }

    const formData = {
        email: email,
        password: password,
        name: name
    };

    fetch('/members', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (!response.ok) {
                alert('Signup request failed');
                throw new Error('Signup request failed');
            }

            return response.json();
        })
        .then(data => {
            console.log('Signup successful:', data);
            window.location.href = '/login';
        })
        .catch(error => {
            console.error('Error during signup:', error);
        });
}


function base64DecodeUnicode(str) {
    const decodedBytes = atob(str);

    const encodedUriComponent = decodedBytes
        .split('')
        .map(function (c) {
            return '%' +
                ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        })
        .join('');

    return decodeURIComponent(encodedUriComponent);
}
