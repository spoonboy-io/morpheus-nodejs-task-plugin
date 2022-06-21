package com.morpheusdata.task

import com.morpheusdata.core.Plugin
import com.morpheusdata.views.HandlebarsRenderer
import com.morpheusdata.views.ViewModel
import com.morpheusdata.model.OptionType

class NodejsTaskPlugin extends Plugin {

	@Override
	String getCode() {
		return 'morpheus-nodejs-task-plugin'
	}

	@Override
	void initialize() {
		NodejsTaskProvider nodejsTaskProvider = new NodejsTaskProvider(this, morpheus)
		this.setName("Node.js Task Plugin")
		this.setDescription("Provides a Node.js script execution task in Morpheus")
		this.setAuthor("Ollie Phillips")
		this.pluginProviders.put(nodejsTaskProvider.code, nodejsTaskProvider)

		// configuration option for the node.js bin executable path
		this.settings << new OptionType(
			name: 'Node.js Path',
			code: 'nodejs-path',
			fieldName: 'nodejsPath',
			displayOrder: 0,
			fieldLabel: 'Path to Node.js',
			helpText: 'Enter the absolute path to the installed Node.js version',
			required: true,
			inputType: OptionType.InputType.TEXT
		)

	}

	@Override
	void onDestroy() {
		morpheus.task.disableTask('nodejsTask')
	}

}