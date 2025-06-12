// JSON data
let data = [];

// Sorting state
let sortColumn = null;
let sortDirection = 1; // 1 for ascending, -1 for descending

$(function () {
    document.getElementById('dataTable').innerHTML = '';
    getRpsData();
    // Add event listeners for sortable columns
    ['turnover_value','pct_change','rps5','rps10', 'rps15', 'rps20', 'rps50'].forEach(column => {
        document.getElementById(`sort-${column}`).addEventListener('click', () => handleSort(column));
    });
})

function getRpsData() {
    $.ajax({

        type: "get",

        url: "getRpsData",

        data: {},

        success: function (reuslt) {
            data = reuslt;
            renderTable();
        }

    });
}

// Function to format turnover value (e.g., 15114400 to 151.1亿)
function formatTurnover(value) {
    return (value / 100000).toFixed(1) + '亿';
}

// Function to format percentage change (e.g., 0.0669553 to 0.07%)
function formatPercentage(value) {
    return (value * 100).toFixed(2) + '%';
}

// Function to get RPS count class for styling
function getRPSCountClass(count) {
    if (count === 5) return 'rps-count-5';
    if (count === 3) return 'rps-count-3';
    if (count === 2) return 'rps-count-2';
    return '';
}

// Function to render the table
function renderTable() {
    const tableBody = document.getElementById('dataTable');
    tableBody.innerHTML = ''; // Clear existing rows

    // Sort data if a sort column is selected
    if (sortColumn) {
        data.sort((a, b) => {
            const valA = a[sortColumn];
            const valB = b[sortColumn];
            return (valA - valB) * sortDirection;
        });
    }

    // Populate the table
    data.forEach((item, index) => {
        const row = document.createElement('tr');
        if (item.block_name === '优刻') {
            row.classList.add('highlight');
        }
        row.innerHTML = `
                    <td>${index + 1}</td>
                    <td class="name">
                        <a href="#">${item.block_name}</a>
                        <span class="rps-count ${getRPSCountClass(item.rps_cnt)}">${item.rps_cnt}</span>
                    </td>
                    <td>${formatTurnover(item.turnover_value)}</td>
                    <td class="pct-change">${formatPercentage(item.pct_change)}</td>
                    <td>${item.rps5}</td>
                    <td>${item.rps10}</td>
                    <td>${item.rps15}</td>
                    <td>${item.rps20}</td>
                    <td>${item.rps50}</td>
                    <td class="operation"><a href="/rpsDataDetailIndex?blockName=${item.block_name}">详情</a></td>
                `;
        tableBody.appendChild(row);
    });
}

// Function to handle sorting
function handleSort(column) {
    const arrow = document.querySelector(`#sort-${column} .sort-arrow`);

    if (sortColumn === column) {
        // Toggle direction if the same column is clicked
        sortDirection *= -1;
    } else {
        // Reset direction for a new column
        sortColumn = column;
        sortDirection = 1;
        // Clear all arrows
        document.querySelectorAll('.sort-arrow').forEach(el => el.innerText = '');
    }

    // Update arrow
    arrow.innerText = sortDirection === 1 ? '↑' : '↓';

    // Re-render the table with sorted data
    renderTable();
}



