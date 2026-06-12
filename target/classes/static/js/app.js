async function init() {
  const accountId = localStorage.getItem("accountId");

  if (!accountId) {
    window.location.href = "/index.html";
    return;
  }

  await loadHUD();
  render("city");
}

init();
