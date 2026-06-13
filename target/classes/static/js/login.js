document.getElementById('loginForm').addEventListener('submit', async (e) => {
  e.preventDefault();
  const login = document.getElementById('login').value;
  const password = document.getElementById('password').value;
  const output = document.getElementById('output');
  const params = new URLSearchParams();
  params.append('username', login);
  params.append('password', password);
  try {
    const response = await fetch('/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: params
    });
    if (response.ok || response.redirected) {
      output.textContent = 'Logged in successfully!';
      output.className = 'output success';

      const charCheck = await fetch('/api/character/exists');
      const hasCharacter = await charCheck.json();
      window.location.href = hasCharacter ? '/city.html' : '/create-character.html';
    } else {
      output.textContent = 'Invalid login or password';
      output.className = 'output error';
    }
  } catch (err) {
    output.textContent = 'Error: ' + err.message;
    output.className = 'output error';
  }
});
