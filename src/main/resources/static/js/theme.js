// Run immediately
(function () {
    let theme = localStorage.getItem("theme") || "dark"; 
    document.body.classList.add(theme);
})();

function toggleTheme() {
    let isDark = document.body.classList.contains("dark");

    if (isDark) {
        document.body.classList.remove("dark");
        document.body.classList.add("light");
        localStorage.setItem("theme", "light");
    } else {
        document.body.classList.remove("light");
        document.body.classList.add("dark");
        localStorage.setItem("theme", "dark");
    }
}