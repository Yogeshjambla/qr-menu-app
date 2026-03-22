const form = document.getElementById('auth-form');
const toggleLink = document.getElementById('toggle-link');
const toggleText = document.getElementById('toggle-text');
const formTitle = document.getElementById('form-title');
const emailField = document.getElementById('email');
const authBtn = document.querySelector('.auth-btn');

let isLogin = true;

toggleLink.addEventListener('click', () => {
  isLogin = !isLogin;
  formTitle.textContent = isLogin ? 'Login' : 'Register';
  toggleText.textContent = isLogin ? "Don't have an account?" : "Already have an account?";
  toggleLink.textContent = isLogin ? "Register" : "Login";
  emailField.style.display = isLogin ? 'none' : 'block';
  authBtn.textContent = isLogin ? 'Login' : 'Register';
});

form.addEventListener('submit', (e) => {
  e.preventDefault();
  const username = form.username.value;
  const password = form.password.value;
  const email = form.email.value;

  if (isLogin) {
    console.log("Logging in with:", { username, password });
    // Make POST request to /api/login
  } else {
    console.log("Registering with:", { username, password, email });
    // Make POST request to /api/register
  }

  // Redirect or show message after success/failure
});
