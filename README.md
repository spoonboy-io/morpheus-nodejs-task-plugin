# Node.js Morpheus Task Plugin

### Write Morpheus automation tasks in Node.js

- Results of previous tasks are in an array (results) of key value objects
- Node.js must be installed on the appliance
- Modules must be installed on the appliance with the global flag `npm install -g modulename`
- The Plugin needs to be configured with the absolute path to the Node installation in the plugin settings

### Setup

1. Install the plugin on Morpheus
2. Install Node.js on the Morpheus Appliance. I used NVM to install Node.js v16.5.1 LTS
3. Discover the absolute path of your Node.js installation (mine was `/home/ollie/.nvm/versions/node/v16.15.1`)
4. Visit the plugin settings (Admin > Integrations > Plugins) and add the absolute path as shown below:- <img width="620" alt="edit plugin settings" src="https://user-images.githubusercontent.com/7113347/174819249-2c9ecd2f-e33e-4b52-a9d5-7da6bc02f580.png">



### Example

A simple script that usinsg AXIOS module - a non-core package

```Node.js
const axios = require('axios');

axios.get('https://jsonplaceholder.typicode.com/users')
  .then(res => {
    const headerDate = res.headers && res.headers.date ? res.headers.date : 'no response date';
    console.log('Status Code:', res.status);
    console.log('Date in Response header:', headerDate);

    const users = res.data;

    for(user of users) {
      console.log(`Got user with id: ${user.id}, name: ${user.name}`);
    }
  })
  .catch(err => {
    console.log('Error: ', err.message);
  });
```

### TODO
- Make the Morpheus vars available to the task
