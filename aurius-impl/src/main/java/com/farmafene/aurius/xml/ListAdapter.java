package com.farmafene.aurius.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.farmafene.aurius.ELMap;

public class ListAdapter implements List<ELMap> {

	private List<ELMap> inner;

	public ListAdapter(List<ELMap> inner) {
		this.inner = inner;
	}

	@Override
	public int size() {
		return inner.size();
	}

	@Override
	public boolean isEmpty() {
		return inner.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<ELMap> iterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(ELMap e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends ELMap> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends ELMap> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ELMap get(int index) {
		return inner.get(index);
	}

	@Override
	public ELMap set(int index, ELMap element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void add(int index, ELMap element) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ELMap remove(int index) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<ELMap> listIterator() {
		return inner.listIterator();
	}

	@Override
	public ListIterator<ELMap> listIterator(int index) {
		return inner.listIterator(index);
	}

	@Override
	public List<ELMap> subList(int fromIndex, int toIndex) {
		return inner.subList(fromIndex, toIndex);
	}

}
