const RESERVATION_API_ENDPOINT = '/reservations-mine';

document.addEventListener('DOMContentLoaded', () => {
  requestRead(RESERVATION_API_ENDPOINT)
      .then(render)
      .catch(error =>
          console.error('Error fetching reservations:', error)
      );
});

function render(data) {
  const tableBody = document.getElementById('table-body');
  tableBody.innerHTML = '';

  data.forEach(item => {
    const row = tableBody.insertRow();

    row.insertCell(0).textContent = item.theme;
    row.insertCell(1).textContent = item.date;
    row.insertCell(2).textContent = item.time;
    row.insertCell(3).textContent = item.status;

    if (item.status !== '예약') {
      const cancelCell = row.insertCell(4);

      const cancelButton = document.createElement('button');
      cancelButton.textContent = '취소';
      cancelButton.className = 'btn btn-danger';

      cancelButton.onclick = function () {
        requestDeleteWaiting(item.id)
            .then(() => window.location.reload());
      };

      cancelCell.appendChild(cancelButton);
    } else {
      row.insertCell(4).textContent = '';
    }
  });
}

function requestRead(endpoint) {
  return fetch(endpoint)
      .then(response => {
        if (response.status === 200) {
          return response.json();
        }

        throw new Error('Read failed');
      });
}

function requestDeleteWaiting(id) {
  const endpoint = '/waitings/' + id;

  return fetch(endpoint, {
    method: 'DELETE'
  })
      .then(response => {
        if (response.status === 204) {
          return;
        }

        throw new Error('Delete failed');
      });
}
