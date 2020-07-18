package jsettlers.algorithms.simplebehaviortree.nodes;

import jsettlers.algorithms.simplebehaviortree.Composite;
import jsettlers.algorithms.simplebehaviortree.Node;
import jsettlers.algorithms.simplebehaviortree.NodeStatus;
import jsettlers.algorithms.simplebehaviortree.Tick;

public class Sequence<T> extends Composite<T> {
	private static final long serialVersionUID = -6313424360855786743L;

	private int index;

	public Sequence(Node<T>[] children) {
		super(children);
	}

	@Override
	protected NodeStatus onTick(Tick<T> tick) {
		for (; index < children.size(); index++) {
			NodeStatus status = children.get(index).execute(tick);

			if(status != NodeStatus.SUCCESS) {
				return status;
			}
		}
		return NodeStatus.SUCCESS;
	}

	@Override
	protected void onOpen(Tick<T> tick) {
		index = 0;
	}
}