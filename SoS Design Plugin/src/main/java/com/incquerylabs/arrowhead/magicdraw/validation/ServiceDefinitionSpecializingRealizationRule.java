package com.incquerylabs.arrowhead.magicdraw.validation;

import arrowhead.validation.ServiceDefinitionSpecializingRealization;
import arrowhead.validation.ServiceDefinitionSpecializingRealization.Match;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;

public class ServiceDefinitionSpecializingRealizationRule extends QueryBasedRule<Match> {
	
	@Override
	public IQuerySpecification<? extends ViatraQueryMatcher<Match>> getQuerySpecification() {
		return ServiceDefinitionSpecializingRealization.instance();
	}
	
	@Override
	public String getMessage(Match match, Constraint constr) {
		return "An SD must not specify an IDD_SP_CP.";
	}
	
	@Override
	public Element getAnnotatedElement(Match match) {
		return match.getGeneralization();
	}
}
