package com.morpheusdata.task

import com.morpheusdata.test.MorpheusContextImpl
import com.morpheusdata.core.ExecutableTaskInterface
import com.morpheusdata.core.MorpheusContext
import com.morpheusdata.core.TaskProvider
import com.morpheusdata.model.OptionType
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class TaskProviderSpec extends Specification {
	@Shared
	MorpheusContext context
	@Shared
	NodejsTaskPlugin plugin
	@Subject
	@Shared
	NodejsTaskProvider provider


	def setup() {
		context = Mock(MorpheusContextImpl)
		plugin = Mock(NodejsTaskPlugin)
		provider = new NodejsTaskProvider(plugin, context)
	}

	void "valid provider"() {
		expect:
		provider
	}

	void "service instantiated"() {
		expect:
		provider instanceof TaskProvider
		provider.service instanceof ExecutableTaskInterface
	}

	void "takes a list of OptionType"() {
		expect:
		provider.optionTypes.size() == 1
		provider.optionTypes[0].inputType == OptionType.InputType.CODE_EDITOR
	}
}
