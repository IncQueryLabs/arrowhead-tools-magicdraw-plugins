package com.incquerylabs.arrowhead.magicdraw.validation;

import arrowhead.validation.SystemDefinitionSpecializingRealization;
import arrowhead.validation.SystemDefinitionSpecializingRealization.Match;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;

public class SystemDefinitionSpecializingRealizationRule extends QueryBasedRule<Match> {
	
	@Override
	public IQuerySpecification<? extends ViatraQueryMatcher<Match>> getQuerySpecification() {
		return SystemDefinitionSpecializingRealization.instance();
	}
	
	@Override
	public String getMessage(Match match, Constraint constr) {
		return "A SysD must not specify a SysDD.";
	}
	
	@Override
	public Element getAnnotatedElement(Match match) {
		return match.getGeneralization();
	}
}
