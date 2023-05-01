package wombatukun.tests.test11.orderservice.dao.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;
import wombatukun.tests.test11.orderservice.dao.entities.Order;
import wombatukun.tests.test11.orderservice.dao.queries.OrderQuery;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderSpec implements Specification<Order> {

	private OrderQuery query;

	public OrderSpec(OrderQuery query) {
		super();
		this.query = query;
	}

	@Override
	public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> expressions = new ArrayList<>();

		if (query.getUserId() != null) {
			expressions.add(cb.equal(root.get("userId"), query.getUserId()));
		}

		if (query.getCourierId() != null) {
			expressions.add(cb.equal(root.get("courierId"), query.getCourierId()));
		}

		if (query.getStatus() != null) {
			expressions.add(cb.equal(root.get("status"), query.getStatus()));
		}

		if (query.getCreatedFrom() != null) {
			expressions.add(cb.greaterThanOrEqualTo(root.get("createdAt"), query.getCreatedFrom()));
		}

		if (query.getCreatedTo() != null) {
			expressions.add(cb.lessThanOrEqualTo(root.get("createdAt"), query.getCreatedTo()));
		}

		return cb.and(expressions.toArray(new Predicate[0]));
	}

}
