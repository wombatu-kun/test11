package wombatukun.tests.test11.orderservice.dao.specifications;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import wombatukun.tests.test11.orderservice.dao.entities.Order;
import wombatukun.tests.test11.orderservice.dao.queries.OrderQuery;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Getter
public class OrderSpec implements Specification<Order> {

	private OrderQuery query;

	public OrderSpec(OrderQuery query) {
		super();
		this.query = query;
	}

	@Override
	public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		Predicate p = cb.conjunction();

		if (query.getUserId() != null) {
			p.getExpressions().add(cb.equal(root.get("userId"), query.getUserId()));
		}

		if (query.getCourierId() != null) {
			p.getExpressions().add(cb.equal(root.get("courierId"), query.getCourierId()));
		}

		if (query.getStatus() != null) {
			p.getExpressions().add(cb.equal(root.get("status"), query.getStatus()));
		}

		if (query.getCreatedFrom() != null) {
			p.getExpressions().add(cb.greaterThanOrEqualTo(root.get("createdAt"), query.getCreatedFrom()));
		}

		if (query.getCreatedTo() != null) {
			p.getExpressions().add(cb.lessThanOrEqualTo(root.get("createdAt"), query.getCreatedTo()));
		}

		return p;
	}

}
