# Node.js Morpheus Task Plugin

### Write Morpheus automation tasks in Node.js

- Results of previous tasks are available in a `results` object variable as `key`:`value`
- Node.js must be installed on the appliance
- Modules must be installed on the appliance with the global flag `npm install -g modulename`
- The Plugin needs to be configured with the absolute path to the Node installation in the plugin settings
- Does not use Nashhorn. Use modern Ecmascript syntax, dependent on the version of Node.js you install

### Build

You should be able to build this locally using the included grade wrapper

```
./gradlew shadowJar     
```

### Setup

1. Install the plugin on Morpheus
2. Install Node.js on the Morpheus Appliance. I used NVM to install Node.js v16.5.1 LTS
3. Discover the absolute path of your Node.js installation (mine was `/home/ollie/.nvm/versions/node/v16.15.1`)
4. Visit the plugin settings (Admin > Integrations > Plugins) and edit the plugin settings to add the absolute path as shown below:- 
<img width="620" alt="edit plugin settings" src="https://user-images.githubusercontent.com/7113347/174819249-2c9ecd2f-e33e-4b52-a9d5-7da6bc02f580.png">

### Example

In this example, we want to use a simple Node.js script in Morpheus. The code requires an external package, Axios, a HTTP request library.

This is the code:-

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

External modules need to be installed on the Morpheus appliance globally. Once installed the module can be used by all Morpheus Node.js tasks.

To install Axios globally we run this command:-

`npm install -g axios`

We can now add the script to a Node.js task. The screenshot below shows it in Morpheus. 

<img width="1131" alt="image" src="https://user-images.githubusercontent.com/7113347/174821605-0760786e-656d-4c3a-9c76-0d1640288ca8.png">

Note how we split the require statements from the main code? This simplifies processing the script on the Morpheus side.

Note also, how we prefix our call for the Axios package? See how `require('axios');` becomes `require('morpheus/axios');`? This tells the task plugin to use the globally installed version of the module, and allows us to differentiate it from core Node.js modules such as `http`

Running this task in Morpheus we should see the following task output:-

<img width="1147" alt="image" src="https://user-images.githubusercontent.com/7113347/174825856-2b5cb056-ad69-4a80-980b-1aaca8340d7d.png">

### TODO
- Make the Morpheus vars available to the task
