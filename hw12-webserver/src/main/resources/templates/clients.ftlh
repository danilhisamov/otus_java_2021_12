<html xmlns="http://www.w3.org/1999/xhtml" lang="ru">
<head>
    <title>Клиенты</title>
    <style>
        .container {
            display: flex;
            flex-direction: row;
        }

        .create-form {
            width: 50%;
        }

        .create-form > input {
            display: block;
            width: 75%;
        }

        .clients-list {
            width: 50%;
        }

        .clients-list > table {
            width: 100%;
            text-align: center;
        }

        .clients-list > table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
    <script>
        function postClient() {
            let client = {
                name: document.getElementById('name').value,
                address: {
                    street: document.getElementById('address').value
                },
                phones: [
                    {
                        number: document.getElementById('phone').value
                    }
                ]
            };

            fetch('/api/clients', {
                method: 'POST',
                body: JSON.stringify(client)
            })
                .then(resp => resp.json())
                .then(json => alert('Created: ' + JSON.stringify(json, null, 1)))
                .then(() => location.reload())
        }
    </script>
</head>

<body>
<div class="container">
    <div class="create-form">
        <h4>Создать клиента</h4>
        <input type="text" id="name" name="name" placeholder="Введите имя">
        <input type="text" id="address" name="address" placeholder="Введите адрес">
        <input type="tel" id="phone" name="phone" placeholder="Введите номер телефона">
        <button onclick="postClient()">Создать</button>
    </div>
    <div class="clients-list">
        <h4>Список клиентов</h4>
        <table id="client-table">
            <thead>
            <tr>
                <td>Id</td>
                <td>Имя</td>
                <td>Адрес</td>
                <td>Телефоны</td>
            </tr>
            </thead>
            <tbody>
            <#list clients as client>
                <tr>
                    <td>${client.id}</td>
                    <td>${client.name}</td>
                    <td>
                        <#if client.address??>
                            <p>ID: ${client.address.id}</p>
                            <p>Улица: ${client.address.street}</p>
                        </#if>
                    </td>
                    <td>
                        <#list client.phones as phone>
                            <div>
                                <p>ID: ${phone.id}</p>
                                <p>Номер: ${phone.number}</p>
                            </div>
                        </#list>
                    </td>
                </tr>
            </#list>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
