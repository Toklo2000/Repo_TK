const form = document.getElementById("registerForm");
const output = document.getElementById("output");

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const login = document.getElementById("login").value.trim();
  const email = document.getElementById("email").value.trim();
  const password = document.getElementById("password").value;
  const date = document.getElementById("date").value.trim();

  const errors = validate(login, email, password, date);

  if (errors.length > 0) {
    output.textContent = errors.join("\n");
    return;
  }

  const payload = { login, email, password, date };

  try {
    const res = await fetch("/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    });

    const text = await res.text();

    if (!res.ok) {
      console.log("ERROR:", text);
      output.textContent = "Server error: " + text;
      return;
    }

    const data = JSON.parse(text);

    localStorage.setItem("accountId", data.id);
    window.location.href = '/index.html';
  
  } catch (err) {
    console.log(err);
    output.textContent = "Network error";
  }
});

function validate(login, email, password, date) {
  const errors = [];

  if (!login) errors.push("Login required");
  else if (login.length < 3 || login.length > 20)
    errors.push("Login must be 3-20 chars");

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!email) errors.push("Email required");
  else if (!emailRegex.test(email))
    errors.push("Invalid email format");

  if (!password) errors.push("Password required");
  else if (password.length < 6)
    errors.push("Password must be at least 6 chars");

  const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/;
  if (!date) errors.push("Date required");
  else if (!dateRegex.test(date))
    errors.push("Date must be dd/mm/yyyy");

  return errors;
}
