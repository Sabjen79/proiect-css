import { refreshAllStores } from '../../../stores/entity';
import type { LayoutLoad } from './$types';

export const load = (async () => {
    await refreshAllStores();
    
    return {};
}) satisfies LayoutLoad;