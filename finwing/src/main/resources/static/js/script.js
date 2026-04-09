document.addEventListener("DOMContentLoaded", () => {

    fetch("/api/dashboard")
        .then(res => res.json())
        .then(data => {

            document.getElementById("m-income").innerText = "₹ " + data.totalIncome;
            document.getElementById("m-expenses").innerText = "₹ " + data.totalExpense;
            document.getElementById("m-balance").innerText = "₹ " + data.balance;
            document.getElementById("m-savings").innerText = data.savingsRate.toFixed(2) + "%";

            document.getElementById("insightText").innerText = data.insight;

            let html = "";
            data.recentTransactions.forEach(t => {
                html += `<p>${t.category} - ₹${t.amount}</p>`;
            });

            document.getElementById("recentList").innerHTML = html;
        });
});