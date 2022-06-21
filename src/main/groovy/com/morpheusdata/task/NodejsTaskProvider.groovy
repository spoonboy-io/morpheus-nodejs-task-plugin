package com.morpheusdata.task

import com.morpheusdata.core.*
import com.morpheusdata.model.OptionType
import com.morpheusdata.model.TaskType
import com.morpheusdata.model.Task
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

@Slf4j
class NodejsTaskProvider implements TaskProvider {
	MorpheusContext morpheusContext
	Plugin plugin
	AbstractTaskService service
	Task task

	NodejsTaskProvider(Plugin plugin, MorpheusContext morpheusContext) {
		this.plugin = plugin
		this.morpheusContext = morpheusContext
		this.task = task
	}

	@Override
	MorpheusContext getMorpheus() {
		return morpheusContext
	}

	@Override
	Plugin getPlugin() {
		return plugin
	}

	@Override
	ExecutableTaskInterface getService() {
		// we make the settings available to the service
		// checking they are set
		def Object settings
		try {
			def pluginSettings = morpheusContext.getSettings(plugin)
			def settingsOutput = ""
			pluginSettings.subscribe({outData -> settingsOutput = outData},{error -> println error.printStackTrace()})
			JsonSlurper slurper = new JsonSlurper()
			settings = slurper.parseText(settingsOutput)
		} catch(Exception ex){
			log.error("Node.js path not set in plugin settings")
			return
		}
		return new NodejsTaskService(morpheusContext, settings)
	}

	@Override
	String getCode() {
		return "nodejsTask"
	}

	@Override
	TaskType.TaskScope getScope() {
		return TaskType.TaskScope.all
	}

	@Override
	String getName() {
		return 'Node.js Script'
	}

	@Override
	String getDescription() {
		return 'Node.js script execution task'
	}

	@Override
	Boolean isAllowExecuteLocal() {
		return true
	}

	@Override
	Boolean isAllowExecuteRemote() {
		return false
	}

	@Override
	Boolean isAllowExecuteResource() {
		return false
	}

	@Override
	Boolean isAllowLocalRepo() {
		return false
	}

	@Override
	Boolean isAllowRemoteKeyAuth() {
		return false
	}

	@Override
	Boolean hasResults() {
		return true
	}

	@Override
	List<OptionType> getOptionTypes() {
		OptionType optionType = new OptionType(
				name: 'nodejsRequire',
				code: 'nodejsTaskRequire',
				fieldName: 'nodejsRequireField',
				displayOrder: 0,
				fieldLabel: 'Node.js Require Script',
				required: false,
				inputType: OptionType.InputType.CODE_EDITOR,
				helpText: 'Enter your module require statements here, use require("morpheus/modulename") for installed modules',
		)
		OptionType optionType2 = new OptionType(
				name: 'nodejsScript',
				code: 'nodejsTaskScript',
				fieldName: 'nodejsScriptField',
				displayOrder: 1,
				fieldLabel: 'Node.js Script',
				required: true,
				inputType: OptionType.InputType.CODE_EDITOR,
				helpText: 'Enter main program code here',
		)
		return [optionType, optionType2]
	}

}
