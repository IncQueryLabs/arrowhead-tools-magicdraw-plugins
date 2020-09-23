package com.incquerylabs.arrowhead.magicdraw.validation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.viatra.query.runtime.api.IPatternMatch;
import org.eclipse.viatra.query.runtime.api.IQuerySpecification;
import org.eclipse.viatra.query.runtime.api.ViatraQueryMatcher;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;

import com.incquerylabs.v4md.ViatraQueryAdapter;
import com.nomagic.actions.NMAction;
import com.nomagic.magicdraw.annotation.Annotation;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.validation.ElementValidationRuleImpl;
import com.nomagic.uml2.ValidationProfile;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Constraint;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.InstanceValue;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Slot;

public abstract class QueryBasedRule<Match extends IPatternMatch> implements ElementValidationRuleImpl {
	
	protected Project project;
	
	@Override
	public void init(Project arg0, Constraint arg1) {
		project = arg0;
	}
	
	@Override
	public Set<Annotation> run(Project project, Constraint constraint, Collection<? extends Element> elements) {
		
		ViatraQueryAdapter adapter =ViatraQueryAdapter.getOrCreateAdapter(project);
		Set<Annotation> annotations = new HashSet<>();
		IQuerySpecification<? extends ViatraQueryMatcher<Match>> spec = getQuerySpecification();
				
		try {
			adapter.getEngine().getMatcher(spec).forEachMatch(match -> {
				Object element = match.get(0);
				if(element != null){
					
					annotations.add(new Annotation(getSeverity(match, constraint), 
								getAnnotationKind(match, constraint), 
								getMessage(match, constraint), 
								getAnnotatedElement(match), 
								getActions(match)));
				}
			});
		} catch (ViatraQueryException e) {
			e.printStackTrace();
		}
				
		return annotations;
	}

	public List<NMAction> getActions(Match match) {
		return Collections.emptyList();
	}
	
	@Override
	public void dispose() {}
	
	public abstract IQuerySpecification<? extends ViatraQueryMatcher<Match>> getQuerySpecification();
	
	public Project getProject() {
		return project;
	}
	
	public EnumerationLiteral getSeverity(Match match, Constraint constr) {
		Project project = getProject();
		if(constr != null){
			Slot severityslot = StereotypesHelper.getSlot(constr, ValidationProfile.getInstance(project).getValidationRule(), "severity", false);
			if(severityslot != null){
				return Annotation.getSeverityLevel(project, ((InstanceValue)severityslot.getValue().iterator().next()).getInstance().getName());
			}
		}
		return Annotation.getSeverityLevel(project, Annotation.ERROR);
	}
	
	public String getAnnotationKind(Match match, Constraint constr) {
		return match.specification().getFullyQualifiedName();
	}
	
	public abstract String getMessage(Match match, Constraint constr);
	
	public abstract Element getAnnotatedElement(Match match);
}