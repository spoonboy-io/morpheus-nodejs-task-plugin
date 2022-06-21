package com.morpheusdata.task

import com.morpheusdata.core.AbstractTaskService
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.model.*

class NodejsTaskService extends AbstractTaskService {
	MorpheusContext context
	Object settings

	NodejsTaskService(MorpheusContext context, Object settings) {
		this.context = context
		this.settings = settings
	}

	@Override
	MorpheusContext getMorpheus() {
		return context
	}

	@Override
	TaskResult executeLocalTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		TaskConfig config = buildLocalTaskConfig([:], task, [], opts).blockingGet()
		executeTask(task, config, opts)
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task, Map opts) {
		return new TaskResult()
	}

	@Override
	TaskResult executeServerTask(ComputeServer server, Task task) {
		return new TaskResult()
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task, Map opts) {
		return new TaskResult()
	}

	@Override
	TaskResult executeContainerTask(Container container, Task task) {
		return new TaskResult()
	}

	@Override
	TaskResult executeRemoteTask(Task task, Map opts, Container container, ComputeServer server, Instance instance) {
		return new TaskResult()
	}

	@Override
	TaskResult executeRemoteTask(Task task, Container container, ComputeServer server, Instance instance) {
		return new TaskResult()
	}

	TaskResult executeTask(Task task, TaskConfig config, Map opts) {
		
		// get the header & script
		def headerOption = task.taskOptions.find { it.optionType.code == 'nodejsTaskRequire' }
		String headerData = headerOption.value
		def scriptOption = task.taskOptions.find { it.optionType.code == 'nodejsTaskScript' }
		String scriptData = scriptOption.value
		
		// TODO we need morpheus vars

		// path
		String modulePath = "${settings.nodejsPath}/lib/node_modules"
		String binPath = "${settings.nodejsPath}/bin/node"
		
		// we need to replace all 'require' calls for external modiles with absolute path
		// modules must be installed globally with -g switch
		// we use a "morpheus" prefix so we can target just these
		String search = "morpheus/"
		String replacement = "${modulePath}/"
		String requireReplace = ""
		if (headerData != null) {
			requireReplace = headerData.replaceAll(search, replacement)
		}
	
		// make dictionaries
		String dictCode = """
let results = {};
let morpheus = {};	
"""

		// check if we have any 
		if (opts.morpheusResults != null) {
			Map inputResults = opts.morpheusResults.getResultMap()

			inputResults.each { key, val ->
				String trimmed = val
				trimmed = trimmed.replace("\r","").replace("\n","")
				dictCode += "results[\"$key\"] = \"$trimmed\";" + "\r\n"
			}		
		}

		// get the dictonary code into the script after requires
		String codeBody = requireReplace + "\r\n" + dictCode + "\r\n" + scriptData
	
		// create the file
		Date ts = new Date();
		String fName = "/tmp/morpheus-nodejs-task-${ts.getTime()}.js"
		File container = new File (fName)

		container << codeBody
		
		def sout = new StringBuilder()
		def serr = new StringBuilder()
		ProcessBuilder pb = new ProcessBuilder(binPath, "${fName}");
		
		Process p = pb.start();
		p.consumeProcessOutput(sout, serr)
		p.waitForOrKill(5 * 1000)

		// clean up
		boolean deleted =  container.delete()  
		
		new TaskResult(
			success: true,
			data   : sout,
			output : sout,
			error : serr
		)
	
	}
	
}
