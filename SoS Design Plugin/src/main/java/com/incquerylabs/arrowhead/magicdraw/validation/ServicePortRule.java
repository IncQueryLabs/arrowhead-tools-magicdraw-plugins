package com.incquerylabs.arrowhead.magicdraw.validation;

import arrowhead.validation.ServicePort;
import arrowhead.validation.ServicePort.Match;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;

import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;


public class ServicePortRule extends QueryBasedRule<Match> {
	
	@Override
	public IQuerySpecification<? extends ViatraQueryMatcher<Match>> getQuerySpecification() {
		return ServicePort.instance();
	}

	@Override
	public String getMessage(Match match, Constraint constr) {
		return "A SysDD must not own a port typed by a SD, only IDD_SP_CPs.";
	}

	@Override
	public Element getAnnotatedElement(Match match) {
		return match.getPort();
	}
}
