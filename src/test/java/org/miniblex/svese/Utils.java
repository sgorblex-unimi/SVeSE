package org.miniblex.svese;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;

public class Utils {
	static <E> E randomElement(Collection<E> s) {
		int rand = new Random().nextInt(s.size());
		int i = 0;
		for (E el : Objects.requireNonNull(s)) {
			if (i == rand)
				return el;
			i++;
		}
		return null;
	}
}
