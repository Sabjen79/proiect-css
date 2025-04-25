import { Discipline, disciplineStore } from '../stores/disciplines';
import type { LayoutLoad } from './$types';

export const load: LayoutLoad = async () => {
	disciplineStore.set([
		new Discipline("123", "Dab1", "dabbb"),
		new Discipline("532", "Dab2", "dabb"),
		new Discipline("724", "Dab3", "dabdbb")
	]);
};