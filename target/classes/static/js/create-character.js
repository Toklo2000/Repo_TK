async function create() {
  const accountId = localStorage.getItem("accountId");
  const name = document.getElementById("name").value;

  if (!accountId || !name) {
    alert("Missing data");
    return;
  }

  const res = await fetch(
    `/api/character/create?accountId=${accountId}&name=${name}`,
    { method: "POST" }
  );

  if (!res.ok) {
    console.log("Create failed");
    return;
  }

  const data = await res.json();
  console.log("CHAR CREATED:", data);

  localStorage.setItem("accountId", accountId);

  window.location.href = "/city.html";
}
